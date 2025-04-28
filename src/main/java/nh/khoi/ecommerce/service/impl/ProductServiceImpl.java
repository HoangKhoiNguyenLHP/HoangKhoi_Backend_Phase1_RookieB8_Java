package nh.khoi.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.entity.Product;
import nh.khoi.ecommerce.mapper.ProductMapper;
import nh.khoi.ecommerce.repository.ProductRepository;
import nh.khoi.ecommerce.request.ProductCreateRequest;
import nh.khoi.ecommerce.service.CloudinaryService;
import nh.khoi.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
