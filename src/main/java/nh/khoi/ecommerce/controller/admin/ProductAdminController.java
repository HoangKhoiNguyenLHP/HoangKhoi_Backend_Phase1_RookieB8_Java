package nh.khoi.ecommerce.controller.admin;

import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.response.ApiResponse;
import nh.khoi.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${pathAdmin}/products")
public class ProductAdminController
{
    private final ProductService productService;

    // [GET] /admin/products
    @GetMapping()
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        List<ProductDto> listProducts = productService.getAllProducts();

        ApiResponse<List<ProductDto>> response = new ApiResponse<>(
                200,
                "Get list products successfully!",
                listProducts
        );
        return ResponseEntity.ok(response);
    }

    // // [Post] /admin/products
    // @PostMapping()
    // public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestBody ProductDto productDto) {
    //     ProductDto savedProduct = productService.createProduct(productDto);
    //
    //     ApiResponse<ProductDto> response = new ApiResponse<>(
    //             201,
    //             "Create product successfully!",
    //             savedProduct
    //     );
    //     return new ResponseEntity<>(response, HttpStatus.CREATED);
    // }
}
