package nh.khoi.ecommerce.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nh.khoi.ecommerce.dto.CategoryDto;
import nh.khoi.ecommerce.dto.ProductDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryGetProductsRequest
{
    private CategoryDto category;
    private List<ProductDto> products;
}
