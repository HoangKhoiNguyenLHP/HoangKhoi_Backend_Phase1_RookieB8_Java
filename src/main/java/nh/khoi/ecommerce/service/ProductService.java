package nh.khoi.ecommerce.service;

import nh.khoi.ecommerce.dto.CategoryDto;
import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.request.ProductCreateRequest;
import nh.khoi.ecommerce.request.ProductEditRequest;
import nh.khoi.ecommerce.response.PaginatedResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService
{
    // -------------- [] -------------- //
    // [GET] /admin/products
    // List<ProductDto> getAllProducts(int page, int limit);
    PaginatedResponse<ProductDto> getAllProducts(int page, int limit, String keyword);

    List<ProductDto> getAllProducts();

    // [GET] /admin/products/:id
    ProductDto getProductById(UUID productId);
    // -------------- End [] -------------- //

    List<ProductDto> getFeaturedProducts();

    // [POST] /admin/products
    ProductDto createProduct(ProductCreateRequest createProductRequest);

    // [PATCH] /admin/products/:id
    ProductDto editProduct(ProductEditRequest updateFields, UUID productId);

    // [DELETE] /admin/products/:id
    void deleteProductSoft(UUID productId);

    // [PATCH] /admin/products/:id/recover
    void recoverProduct(UUID productId);

    // [DELETE] /admin/products/:id/permanent
    void deleteProductPermanent(UUID productId);
}
