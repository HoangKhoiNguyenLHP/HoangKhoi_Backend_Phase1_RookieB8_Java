package nh.khoi.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.entity.Product;
import nh.khoi.ecommerce.mapper.ProductMapper;
import nh.khoi.ecommerce.repository.ProductRepository;
import nh.khoi.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService
{
    private final ProductRepository productRepository;

    // [GET] /admin/products
    @Override
    public List<ProductDto> getAllProducts()
    {
        List<Product> listProducts = productRepository.findAll();

        return listProducts.stream()
                .map((eachProduct) -> ProductMapper.mapToProductDto(eachProduct))
                .collect(Collectors.toList());
    }

    // // [POST] /admin/products
    // @Override
    // public ProductDto createProduct(ProductDto productDto)
    // {
    //     Product product = ProductMapper.mapToProduct(productDto);
    //     Product savedProduct = productRepository.save(product);
    //
    //     return ProductMapper.mapToProductDto(savedProduct);
    // }
}
