package nh.khoi.ecommerce.service;

import nh.khoi.ecommerce.dto.CategoryDto;
import nh.khoi.ecommerce.entity.Category;
import nh.khoi.ecommerce.request.CategoryTreeRequest;
import nh.khoi.ecommerce.response.PaginatedResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CategoryService
{
    // -------------- [] -------------- //
    // [GET] /admin/categories
    // List<CategoryDto> getAllCategories(int page, int limit);
    PaginatedResponse<CategoryDto> getAllCategories(int page, int limit);

    // [GET] /admin/categories/:id
    CategoryDto getCategoryById(UUID categoryId);
    // -------------- End [] -------------- //


    // -------------- [] -------------- //
    List<CategoryTreeRequest> buildCategoryTree(List<Category> categories, UUID parentId);

    // [GET] /admin/categories/create
    List<CategoryTreeRequest> getCategoryCreatePageData();

    // [Post] /admin/categories
    CategoryDto createCategory(CategoryDto categoryDto);
    // -------------- End [] -------------- //


    // -------------- [] -------------- //
    // [PATCH] /admin/categories/:id
    CategoryDto editCategory(Map<String, Object> updateFields, UUID categoryId);
    // -------------- End [] -------------- //


    // -------------- [] -------------- //
    // [DELETE] /admin/categories/:id
    void deleteCategorySoft(UUID categoryId);

    // [PATCH] /admin/categories/:id/recover
    void recoverCategory(UUID categoryId);

    // [DELETE] /admin/categories/:id/permanent
    void deleteCategoryPermanent(UUID categoryId);
    // -------------- End [] -------------- //
}
