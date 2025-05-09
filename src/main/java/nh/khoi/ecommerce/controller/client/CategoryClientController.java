package nh.khoi.ecommerce.controller.client;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.request.CategoryGetProductsRequest;
import nh.khoi.ecommerce.request.CategoryTreeRequest;
import nh.khoi.ecommerce.response.ApiResponse;
import nh.khoi.ecommerce.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/client/categories")
public class CategoryClientController
{
    private final CategoryService categoryService;

    // [GET] /api/client/categories
    @Operation(summary = "Get all categories but format as tree")
    @GetMapping()
    public ResponseEntity<ApiResponse<List<CategoryTreeRequest>>> getCategoryTree() {
        List<CategoryTreeRequest> tree = categoryService.getCategoryCreatePageData();

        ApiResponse<List<CategoryTreeRequest>> response = new ApiResponse<>(
                200,
                "Get category tree successfully!",
                tree
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all products that belongs to the category")
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<CategoryGetProductsRequest>> getProductsByCategory(@PathVariable String slug) {
        CategoryGetProductsRequest data = categoryService.getProductsByCategorySlug(slug);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Fetched category products", data)
        );
    }
}
