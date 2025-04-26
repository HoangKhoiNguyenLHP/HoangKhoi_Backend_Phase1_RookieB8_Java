package nh.khoi.ecommerce.service;

import nh.khoi.ecommerce.dto.ProductDto;

import java.util.List;

public interface ProductService
{
    // [GET] /admin/products
    List<ProductDto> getAllProducts();

    // // [POST] /admin/products
    // ProductDto createProduct(ProductDto productDto);
}
