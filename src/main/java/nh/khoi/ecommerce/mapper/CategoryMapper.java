package nh.khoi.ecommerce.mapper;


import nh.khoi.ecommerce.dto.CategoryDto;
import nh.khoi.ecommerce.entity.Category;

public class CategoryMapper
{
    public static CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getParent(),
                category.getDescription(),
                category.getPosition(),
                category.getDeleted(),
                category.getSlug(),
                category.getCreatedOn(),
                category.getUpdatedOn()
        );
    }

    public static Category mapToCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getId(),
                categoryDto.getName(),
                categoryDto.getParent(),
                categoryDto.getDescription(),
                categoryDto.getPosition(),
                categoryDto.getDeleted(),
                categoryDto.getSlug(),
                categoryDto.getCreatedOn(),
                categoryDto.getUpdatedOn()
        );
    }
}
