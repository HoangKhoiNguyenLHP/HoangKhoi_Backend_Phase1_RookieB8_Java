package nh.khoi.ecommerce.mapper;

import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.entity.Product;

public class ProductMapper
{
    public static ProductDto mapToProductDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImages(),
                product.getIsFeatured(),
                product.getDeleted(),
                product.getCreatedOn(),
                product.getUpdatedOn()
        );
    }

    public static Product mapToProduct(ProductDto productDto) {
        return new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getImages(),
                productDto.getIsFeatured(),
                productDto.getDeleted(),
                productDto.getCreatedOn(),
                productDto.getUpdatedOn()
        );
    }
}
