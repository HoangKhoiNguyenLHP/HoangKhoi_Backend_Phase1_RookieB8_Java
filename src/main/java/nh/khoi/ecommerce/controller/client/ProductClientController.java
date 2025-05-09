package nh.khoi.ecommerce.controller.client;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.AccountAdminDto;
import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.response.ApiResponse;
import nh.khoi.ecommerce.response.PaginatedResponse;
import nh.khoi.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/client/products")
public class ProductClientController
{
    private final ProductService productService;

    // [GET] /api/client/products
    @Operation(summary = "Get all products with paging")
    @GetMapping()
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductDto>>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String keyword
    )
    {
        // List<ProductDto> listProducts = productService.getAllProducts(page, limit);
        PaginatedResponse<ProductDto> listProducts = productService.getAllProducts(page, limit, keyword);

        ApiResponse<PaginatedResponse<ProductDto>> response = new ApiResponse<>(
                200,
                "Get list products successfully!",
                listProducts
        );
        return ResponseEntity.ok(response);
    }

    // [GET] /api/client/products/featured
    @Operation(summary = "Get featured products")
    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getFeaturedProducts() {
        List<ProductDto> listProducts = productService.getFeaturedProducts();

        ApiResponse<List<ProductDto>> response = new ApiResponse<>(
                200,
                "Get list featured products successfully!",
                listProducts
        );
        return ResponseEntity.ok(response);
    }
}
