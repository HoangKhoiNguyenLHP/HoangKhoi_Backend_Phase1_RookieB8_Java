package nh.khoi.ecommerce.mapper;

import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.entity.Category;
import nh.khoi.ecommerce.entity.Product;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductMapper
{
    public static ProductDto mapToProductDto(Product product) {
        List<UUID> categoryIds = product.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toList());

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImages(),
                product.getFeatured(),
                product.getPosition(),
                product.getStock(),
                product.getDeleted(),
                product.getSlug(),
                product.getCreatedOn(),
                product.getUpdatedOn(),
                categoryIds
        );
    }

    public static Product mapToProduct(ProductDto productDto) {
        Product product = new Product();

        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImages(productDto.getImages());
        product.setFeatured(productDto.getIsFeatured());
        product.setPosition(productDto.getPosition());
        product.setStock(productDto.getStock());
        product.setDeleted(productDto.getDeleted());
        product.setSlug(productDto.getSlug());
        product.setCreatedOn(productDto.getCreatedOn());
        product.setUpdatedOn(productDto.getUpdatedOn());

        return product;
    }
}
