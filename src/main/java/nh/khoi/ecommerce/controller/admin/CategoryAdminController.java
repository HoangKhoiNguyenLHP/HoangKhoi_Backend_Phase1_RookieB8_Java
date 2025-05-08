package nh.khoi.ecommerce.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.AccountAdminDto;
import nh.khoi.ecommerce.dto.CategoryDto;
import nh.khoi.ecommerce.request.CategoryTreeRequest;
import nh.khoi.ecommerce.response.ApiResponse;
import nh.khoi.ecommerce.response.PaginatedResponse;
import nh.khoi.ecommerce.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${pathAdmin}/categories")
public class CategoryAdminController
{
    private final CategoryService categoryService;

    // -------------- [] -------------- //
    // [GET] /admin/categories
    @Operation(summary = "Get all categories")
    @GetMapping()
    public ResponseEntity<ApiResponse<PaginatedResponse<CategoryDto>>> getAllCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String keyword
    )
    {
        // List<CategoryDto> listCategories = categoryService.getAllCategories(page, limit);
        PaginatedResponse<CategoryDto> listCategories = categoryService.getAllCategories(page, limit, keyword);

        ApiResponse<PaginatedResponse<CategoryDto>> response = new ApiResponse<>(
                200,
                "Get list categories successfully!",
                listCategories
        );
        return ResponseEntity.ok(response);
    }

    // [GET] /admin/categories/:id
    @Operation(summary = "Get a category detail")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryById(@PathVariable("id") UUID categoryId) {
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);

        ApiResponse<CategoryDto> response = new ApiResponse<>(
                200,
                "Get category detail successfully!",
                categoryDto
        );
        return ResponseEntity.ok(response);
    }
    // -------------- End [] -------------- //


    // -------------- [] -------------- //
    // [GET] /admin/categories/create
    @Operation(summary = "Get all categories but format as tree")
    @GetMapping("/create")
    public ResponseEntity<ApiResponse<List<CategoryTreeRequest>>> getCategoryCreatePage() {
        List<CategoryTreeRequest> tree = categoryService.getCategoryCreatePageData();

        ApiResponse<List<CategoryTreeRequest>> response = new ApiResponse<>(
                200,
                "Get category tree successfully!",
                tree
        );

        return ResponseEntity.ok(response);
    }

    // [Post] /admin/categories
    @Operation(summary = "Create a category")
    @PostMapping()
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(
            @RequestBody @Valid CategoryDto categoryDto,
            BindingResult bindingResult
    )
    {
        if(bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            ApiResponse<CategoryDto> response = new ApiResponse<>(400, errorMessage, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        CategoryDto savedCategory = categoryService.createCategory(categoryDto);
        ApiResponse<CategoryDto> response = new ApiResponse<>(
                201,
                "Create category successfully!",
                savedCategory
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // -------------- End [] -------------- //


    // -------------- [] -------------- //
    // [PATCH] /admin/categories/:id
    @Operation(summary = "Update a category")
    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> editCategory(
            @RequestBody Map<String, Object> updateFields,
            @PathVariable("id") UUID productId
    )
    {
        CategoryDto updatedCategory = categoryService.editCategory(updateFields, productId);
        ApiResponse<CategoryDto> response = new ApiResponse<>(
                200,
                "Update category successfully!",
                updatedCategory
        );
        return ResponseEntity.ok(response);
    }
    // -------------- End [] -------------- //


    // -------------- [] -------------- //
    // [DELETE] /admin/categories/:id
    @Operation(summary = "Delete soft a category")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategorySoft(@PathVariable("id") UUID categoryId) {
        categoryService.deleteCategorySoft(categoryId);

        ApiResponse<Void> response = new ApiResponse<>(
                200,
                "Delete soft category successfully!",
                null
        );
        return ResponseEntity.ok(response);
    }

    // [PATCH] /admin/categories/:id/recover
    @Operation(summary = "Recover a category")
    @PatchMapping("{id}/recover")
    public ResponseEntity<ApiResponse<Void>> recoverCategory(@PathVariable("id") UUID categoryId) {
        categoryService.recoverCategory(categoryId);

        ApiResponse<Void> response = new ApiResponse<>(
                200,
                "Recover category successfully!",
                null
        );
        return ResponseEntity.ok(response);
    }

    // [DELETE] /admin/categories/:id/permanent
    @Operation(summary = "Delete permanently a category")
    @DeleteMapping("{id}/permanent")
    public ResponseEntity<ApiResponse<Void>> deleteProductPermanent(@PathVariable("id") UUID categoryId) {
        categoryService.deleteCategoryPermanent(categoryId);

        ApiResponse<Void> response = new ApiResponse<>(
                200,
                "Delete category permanently successfully!",
                null
        );
        return ResponseEntity.ok(response);
    }
    // -------------- End [] -------------- //
}
