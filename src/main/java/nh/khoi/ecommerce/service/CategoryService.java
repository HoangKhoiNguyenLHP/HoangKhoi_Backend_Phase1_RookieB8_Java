package nh.khoi.ecommerce.service;

import nh.khoi.ecommerce.dto.CategoryDto;
import nh.khoi.ecommerce.response.PaginatedResponse;

import java.util.Map;
import java.util.UUID;

public interface CategoryService
{
    // [GET] /admin/categories
    // List<CategoryDto> getAllCategories(int page, int limit);
    PaginatedResponse<CategoryDto> getAllCategories(int page, int limit);

    // [Post] /admin/categories
    CategoryDto createCategory(CategoryDto categoryDto);

    // [PATCH] /admin/categories/:id
    CategoryDto editCategory(Map<String, Object> updateFields, UUID categoryId);

    // [DELETE] /admin/categories/:id
    void deleteCategorySoft(UUID categoryId);

    // [PATCH] /admin/categories/:id/recover
    void recoverCategory(UUID categoryId);

    // [DELETE] /admin/categories/:id/permanent
    void deleteCategoryPermanent(UUID categoryId);
}
