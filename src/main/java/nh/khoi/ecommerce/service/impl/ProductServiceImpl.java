package nh.khoi.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.CategoryDto;
import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.entity.Category;
import nh.khoi.ecommerce.entity.Product;
import nh.khoi.ecommerce.exception.BadRequestException;
import nh.khoi.ecommerce.exception.ResourceNotFoundException;
import nh.khoi.ecommerce.mapper.CategoryMapper;
import nh.khoi.ecommerce.mapper.ProductMapper;
import nh.khoi.ecommerce.repository.CategoryRepository;
import nh.khoi.ecommerce.repository.ProductRepository;
import nh.khoi.ecommerce.request.ProductCreateRequest;
import nh.khoi.ecommerce.request.ProductEditRequest;
import nh.khoi.ecommerce.response.PaginatedResponse;
import nh.khoi.ecommerce.service.CloudinaryService;
import nh.khoi.ecommerce.service.ProductService;
import nh.khoi.ecommerce.utils.SlugUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService
{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    // -------------- [] -------------- //
    // [GET] /admin/products
    @Override
    public PaginatedResponse<ProductDto> getAllProducts(int page, int limit, String keyword)
    {
        // ----- Pagination ----- //
        Pageable pageable = PageRequest.of(
                page - 1,
                limit,
                Sort.by("position").descending()
        );

        Page<Product> listProducts = productRepository.findAllByDeletedFalse(pageable);
        // ----- End pagination ----- //


        // ----- Search ----- //
        if (keyword != null && !keyword.trim().isEmpty()) {
            String slugifiedKeyword = SlugUtil.toSlug(keyword);
            listProducts = productRepository.findBySlugContainingIgnoreCaseAndDeletedFalse(slugifiedKeyword, pageable);
        }
        // ----- End search ----- //


        List<ProductDto> productDtos = listProducts
                .getContent()
                .stream()
                .map((eachProduct) -> ProductMapper.mapToProductDto(eachProduct))
                .collect(Collectors.toList());

        PaginatedResponse<ProductDto> response = new PaginatedResponse<>(
                listProducts.getNumber() + 1,
                listProducts.getTotalPages(),
                listProducts.getTotalElements(),
                (page - 1) * limit,
                productDtos
        );

        return response;
    }

    @Override
    public List<ProductDto> getAllProducts()
    {
        List<Product> listProducts = productRepository.findAllByDeletedFalse();
        return listProducts.stream()
                .map((item) -> ProductMapper.mapToProductDto(item))
                .collect(Collectors.toList());
    }

    // [GET] /admin/categories/:id
    @Override
    public ProductDto getProductById(UUID productId)
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with given id: " + productId
                ));

        return ProductMapper.mapToProductDto(product);
    }
    // -------------- End [] -------------- //

    @Override
    public List<ProductDto> getFeaturedProducts()
    {
        List<Product> listFeaturedProducts = productRepository.findTop6ByFeaturedAndDeletedOrderByPositionDesc(true, false);
        return listFeaturedProducts.stream()
                .map((item) -> ProductMapper.mapToProductDto(item))
                .collect(Collectors.toList());
    }

    // [POST] /admin/products
    @Override
    public ProductDto createProduct(ProductCreateRequest createProductRequest)
    {
        Product product = new Product();
        product.setName(createProductRequest.getName());
        product.setDescription(createProductRequest.getDescription());
        product.setPrice(createProductRequest.getPrice() != null ? createProductRequest.getPrice() : 0.0);
        product.setStock(createProductRequest.getStock() != null ? createProductRequest.getStock() : 0);
        product.setFeatured(createProductRequest.getIsFeatured() != null ? createProductRequest.getIsFeatured() : false);

        if(createProductRequest.getPosition() == null) {
            Optional<Product> productWithMaxPosition = productRepository.findTopByOrderByPositionDesc();

            int newPosition = productWithMaxPosition
                    .map(item -> item.getPosition() != null ? item.getPosition() + 1 : 1)
                    .orElse(1);
            product.setPosition(newPosition);
        }
        else {
            product.setPosition(createProductRequest.getPosition()); // <-- THIS is crucial
        }

        String baseSlug = SlugUtil.toSlug(createProductRequest.getName());
        String uniqueSlug = SlugUtil.generateUniqueSlug(baseSlug, slug -> productRepository.existsBySlug(slug));
        product.setSlug(uniqueSlug);

        // Upload images
        if (createProductRequest.getImages() != null && !createProductRequest.getImages().isEmpty()) {
            List<String> imageUrls = createProductRequest.getImages().stream()
                    .map(file -> cloudinaryService.uploadFile(file))
                    .toList();
            product.setImages(imageUrls);
        }

        // Set categories
        if(createProductRequest.getCategoryIds() != null && !createProductRequest.getCategoryIds().isEmpty()) {
            Set<Category> categories = createProductRequest.getCategoryIds().stream()
                    .map(id -> categoryRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id)))
                    .collect(Collectors.toSet());
            product.setCategories(categories);
        }

        Product savedProduct = productRepository.save(product);
        return ProductMapper.mapToProductDto(savedProduct);
    }

    // [PATCH] /admin/products/:id
    @Override
    public ProductDto editProduct(ProductEditRequest updateFields, UUID productId)
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with given id: " + productId
                ));

        // ----- Validation manually [PATCH] ----- //
        if(updateFields.getName() != null && updateFields.getName().trim().isEmpty()) {
            throw new BadRequestException("Product name is required!");
        }

        if(updateFields.getPrice() != null && updateFields.getPrice() < 0) {
            throw new BadRequestException("Price cannot be negative!");
        }

        if(updateFields.getStock() != null && updateFields.getStock() < 0) {
            throw new BadRequestException("Stock cannot be negative!");
        }
        // ----- End validation manually [PATCH] ----- //

        if(updateFields.getName() != null) {
            product.setName(updateFields.getName());
            // update slug
            String baseSlug = SlugUtil.toSlug(updateFields.getName());
            String uniqueSlug = SlugUtil.generateUniqueSlug(baseSlug, slug -> productRepository.existsBySlug(slug));
            product.setSlug(uniqueSlug);
        }

        if(updateFields.getDescription() != null) {
            product.setDescription(updateFields.getDescription());
        }

        if(updateFields.getPrice() != null) {
            product.setPrice(updateFields.getPrice());
        }

        if(updateFields.getStock() != null) {
            product.setStock(updateFields.getStock());
        }

        if(updateFields.getIsFeatured() != null) {
            product.setFeatured(updateFields.getIsFeatured());
        }

        if(updateFields.getPosition() != null && !updateFields.getPosition().toString().isBlank()) {
            try {
                product.setPosition(Integer.parseInt(updateFields.getPosition().toString()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Position must be an integer!");
            }
        }

        // ---- old
        //
        // if(updateFields.getImages() != null && updateFields.getImages().length > 0) {
        //     List<String> imageUrls = Arrays.stream(updateFields.getImages())
        //             .map(file -> cloudinaryService.uploadFile(file))
        //             .toList();
        //     product.getImages().addAll(imageUrls);
        // }

        // Handle images from FilePond
        if(updateFields.getImages() != null || updateFields.getExistingImageUrls() != null)
        {
            List<String> newImageUrls = new ArrayList<>();

            // Handle new file uploads
            if(updateFields.getImages() != null && updateFields.getImages().length > 0) {
                List<String> uploadedUrls = Arrays.stream(updateFields.getImages())
                        .map(file -> cloudinaryService.uploadFile(file))
                        .toList();
                newImageUrls.addAll(uploadedUrls);
            }

            // Handle remaining old image URLs
            if(updateFields.getExistingImageUrls() != null && !updateFields.getExistingImageUrls().isEmpty()) {
                List<String> existingUrls = updateFields.getExistingImageUrls(); // parse this if sent as JSON string
                newImageUrls.addAll(existingUrls);
            }

            // Replace images
            product.setImages(newImageUrls);
        }

        // categories
        if(updateFields.getCategoryIds() != null) {
            Set<Category> categories = updateFields.getCategoryIds()
                    .stream()
                    .map(id -> categoryRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Category not found with id: " + id)))
                    .collect(Collectors.toSet());
            product.setCategories(categories); // overwrite existing categories
        }

        Product updatedProduct = productRepository.save(product);
        return ProductMapper.mapToProductDto(updatedProduct);
    }

    // [DELETE] /admin/products/:id
    @Override
    public void deleteProductSoft(UUID productId)
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with given id: " + productId
                ));

        product.setDeleted(true);

        productRepository.save(product);
    }

    // [PATCH] /admin/products/:id/recover
    @Override
    public void recoverProduct(UUID productId)
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with given id: " + productId
                ));

        product.setDeleted(false);

        productRepository.save(product);
    }

    // [DELETE] /admin/products/:id/permanent
    @Override
    public void deleteProductPermanent(UUID productId)
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with given id: " + productId
                ));

        productRepository.deleteById(productId);
    }
}
