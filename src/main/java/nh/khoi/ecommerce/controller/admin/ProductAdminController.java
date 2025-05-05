package nh.khoi.ecommerce.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.request.ProductCreateRequest;
import nh.khoi.ecommerce.request.ProductEditRequest;
import nh.khoi.ecommerce.response.ApiResponse;
import nh.khoi.ecommerce.response.PaginatedResponse;
import nh.khoi.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${pathAdmin}/products")
public class ProductAdminController
{
    private final ProductService productService;

    // [GET] /admin/products
    @GetMapping()
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductDto>>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int limit
    )
    {
        // List<ProductDto> listProducts = productService.getAllProducts(page, limit);
        PaginatedResponse<ProductDto> listProducts = productService.getAllProducts(page, limit);

        ApiResponse<PaginatedResponse<ProductDto>> response = new ApiResponse<>(
                200,
                "Get list products successfully!",
                listProducts
        );
        return ResponseEntity.ok(response);
    }

    // [POST] /admin/products
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(
            @ModelAttribute @Valid ProductCreateRequest createProductRequest,
            BindingResult bindingResult
    )
    {
        if(bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            ApiResponse<ProductDto> response = new ApiResponse<>(400, errorMessage, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        ProductDto savedProduct = productService.createProduct(createProductRequest);
        ApiResponse<ProductDto> response = new ApiResponse<>(
                201,
                "Create product successfully!",
                savedProduct
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // [PATCH] /admin/products/:id
    @PatchMapping(path = "{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ProductDto>> editProduct(
            @ModelAttribute ProductEditRequest updateFields,
            @PathVariable("id") UUID productId
    )
    {
        ProductDto updatedProduct = productService.editProduct(updateFields, productId);
        ApiResponse<ProductDto> response = new ApiResponse<>(
                200,
                "Update product successfully!",
                updatedProduct
        );
        return ResponseEntity.ok(response);
    }
}
