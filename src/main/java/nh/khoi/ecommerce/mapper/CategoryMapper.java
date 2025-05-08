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
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setParent(categoryDto.getParent());
        category.setDescription(categoryDto.getDescription());
        category.setPosition(categoryDto.getPosition());
        category.setDeleted(categoryDto.getDeleted());
        category.setSlug(categoryDto.getSlug());
        category.setCreatedOn(categoryDto.getCreatedOn());
        category.setUpdatedOn(categoryDto.getUpdatedOn());

        return category;
    }
}
