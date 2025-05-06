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
                product.getPosition(),
                product.getDeleted(),
                product.getSlug(),
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
                productDto.getPosition(),
                productDto.getDeleted(),
                productDto.getSlug(),
                productDto.getCreatedOn(),
                productDto.getUpdatedOn()
        );
    }
}
