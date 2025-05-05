package nh.khoi.ecommerce.service;

import nh.khoi.ecommerce.dto.CategoryDto;
import nh.khoi.ecommerce.response.PaginatedResponse;

public interface CategoryService
{
    // [GET] /admin/categories
    // List<CategoryDto> getAllCategories(int page, int limit);
    PaginatedResponse<CategoryDto> getAllCategories(int page, int limit);
}
