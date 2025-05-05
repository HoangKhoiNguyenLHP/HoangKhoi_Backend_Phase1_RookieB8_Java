package nh.khoi.ecommerce.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.CategoryDto;
import nh.khoi.ecommerce.response.ApiResponse;
import nh.khoi.ecommerce.response.PaginatedResponse;
import nh.khoi.ecommerce.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${pathAdmin}/categories")
public class CategoryAdminController
{
    private final CategoryService categoryService;

    // [GET] /admin/categories
    @Operation(summary = "Get all categories")
    @GetMapping()
    public ResponseEntity<ApiResponse<PaginatedResponse<CategoryDto>>> getAllCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int limit
    )
    {
        // List<CategoryDto> listCategories = categoryService.getAllCategories(page, limit);
        PaginatedResponse<CategoryDto> listCategories = categoryService.getAllCategories(page, limit);

        ApiResponse<PaginatedResponse<CategoryDto>> response = new ApiResponse<>(
                200,
                "Get list categories successfully!",
                listCategories
        );
        return ResponseEntity.ok(response);
    }
}
