package nh.khoi.ecommerce.service;

import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.request.ProductCreateRequest;
import nh.khoi.ecommerce.request.ProductEditRequest;

import java.util.List;
import java.util.UUID;

public interface ProductService
{
    // [GET] /admin/products
    List<ProductDto> getAllProducts();

    // [POST] /admin/products
    ProductDto createProduct(ProductCreateRequest createProductRequest);

    // [PATCH] /admin/products/:id
    ProductDto editProduct(ProductEditRequest updateFields, UUID productId);
}
