package nh.khoi.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.CategoryDto;
import nh.khoi.ecommerce.entity.Category;
import nh.khoi.ecommerce.mapper.CategoryMapper;
import nh.khoi.ecommerce.repository.CategoryRepository;
import nh.khoi.ecommerce.response.PaginatedResponse;
import nh.khoi.ecommerce.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository categoryRepository;

    // [GET] /admin/categories
    @Override
    public PaginatedResponse<CategoryDto> getAllCategories(int page, int limit)
    {
        Pageable pageable = PageRequest.of(page - 1, limit);

        Page<Category> listCategories = categoryRepository.findAllByDeletedFalse(pageable);

        List<CategoryDto> categoryDtos = listCategories
                .getContent()
                .stream()
                .map((eachCategory) -> CategoryMapper.mapToCategoryDto(eachCategory))
                .collect(Collectors.toList());

        PaginatedResponse<CategoryDto> response = new PaginatedResponse<>(
                listCategories.getTotalPages(),
                listCategories.getTotalElements(),
                (page - 1) * limit,
                categoryDtos
        );

        return response;
    }
}
