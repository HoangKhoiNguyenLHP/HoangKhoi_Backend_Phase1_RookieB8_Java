package nh.khoi.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.entity.AccountAdmin;
import nh.khoi.ecommerce.entity.Product;
import nh.khoi.ecommerce.exception.BadRequestException;
import nh.khoi.ecommerce.exception.ResourceNotFoundException;
import nh.khoi.ecommerce.mapper.ProductMapper;
import nh.khoi.ecommerce.repository.ProductRepository;
import nh.khoi.ecommerce.request.ProductCreateRequest;
import nh.khoi.ecommerce.request.ProductEditRequest;
import nh.khoi.ecommerce.response.PaginatedResponse;
import nh.khoi.ecommerce.service.CloudinaryService;
import nh.khoi.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService
{
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    // [GET] /admin/products
    @Override
    public PaginatedResponse<ProductDto> getAllProducts(int page, int limit)
    {
        Pageable pageable = PageRequest.of(page - 1, limit);

        Page<Product> listProducts = productRepository.findAllByDeletedFalse(pageable);

        List<ProductDto> productDtos = listProducts
                .getContent()
                .stream()
                .map((eachProduct) -> ProductMapper.mapToProductDto(eachProduct))
                .collect(Collectors.toList());

        PaginatedResponse<ProductDto> response = new PaginatedResponse<>(
                listProducts.getTotalPages(),
                listProducts.getTotalElements(),
                (page - 1) * limit,
                productDtos
        );

        return response;
    }

    // [POST] /admin/products
    @Override
    public ProductDto createProduct(ProductCreateRequest createProductRequest)
    {
        Product product = new Product();
        product.setName(createProductRequest.getName());
        product.setDescription(createProductRequest.getDescription());
        product.setPrice(createProductRequest.getPrice() != null ? createProductRequest.getPrice() : 0.0);
        product.setIsFeatured(createProductRequest.getIsFeatured() != null ? createProductRequest.getIsFeatured() : false);

        // Upload images
        if (createProductRequest.getImages() != null && !createProductRequest.getImages().isEmpty()) {
            List<String> imageUrls = createProductRequest.getImages().stream()
                    .map(file -> cloudinaryService.uploadFile(file))
                    .toList();
            product.setImages(imageUrls);
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

        // ----- Validation manually ----- //
        if(updateFields.getName() != null && updateFields.getName().trim().isEmpty()) {
            throw new BadRequestException("Product name is required!");
        }

        if(updateFields.getPrice() != null && updateFields.getPrice() < 0) {
            throw new BadRequestException("Price cannot be negative!");
        }
        // ----- End validation manually ----- //

        if(updateFields.getName() != null) {
            product.setName(updateFields.getName());
        }

        if(updateFields.getDescription() != null) {
            product.setDescription(updateFields.getDescription());
        }

        if(updateFields.getPrice() != null) {
            product.setPrice(updateFields.getPrice());
        }

        if(updateFields.getIsFeatured() != null) {
            product.setIsFeatured(updateFields.getIsFeatured());
        }

        // ---- old
        //
        // if(updateFields.getImages() != null && !updateFields.getImages().isEmpty()) {
        //     List<String> imageUrls = updateFields.getImages().stream()
        //             .map(file -> cloudinaryService.uploadFile(file))
        //             .toList();
        //     product.getImages().addAll(imageUrls);
        // }

        // ---- new
        //
        if(updateFields.getImages() != null && updateFields.getImages().length > 0) {
            List<String> imageUrls = Arrays.stream(updateFields.getImages())
                    .map(file -> cloudinaryService.uploadFile(file))
                    .toList();
            product.getImages().addAll(imageUrls);
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
