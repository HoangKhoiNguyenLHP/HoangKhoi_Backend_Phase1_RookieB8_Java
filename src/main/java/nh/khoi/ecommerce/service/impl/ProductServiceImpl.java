package nh.khoi.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.entity.Product;
import nh.khoi.ecommerce.exception.ResourceNotFoundException;
import nh.khoi.ecommerce.mapper.ProductMapper;
import nh.khoi.ecommerce.repository.ProductRepository;
import nh.khoi.ecommerce.request.ProductCreateRequest;
import nh.khoi.ecommerce.request.ProductEditRequest;
import nh.khoi.ecommerce.service.CloudinaryService;
import nh.khoi.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public List<ProductDto> getAllProducts()
    {
        List<Product> listProducts = productRepository.findAll();

        return listProducts.stream()
                .map((eachProduct) -> ProductMapper.mapToProductDto(eachProduct))
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
                        "Product does not exist with given id: " + productId
                ));

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
}
