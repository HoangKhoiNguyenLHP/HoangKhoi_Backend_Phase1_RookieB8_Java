package nh.khoi.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.CategoryDto;
import nh.khoi.ecommerce.entity.Category;
import nh.khoi.ecommerce.mapper.CategoryMapper;
import nh.khoi.ecommerce.repository.CategoryRepository;
import nh.khoi.ecommerce.response.PaginatedResponse;
import nh.khoi.ecommerce.service.CategoryService;
import nh.khoi.ecommerce.utils.SlugUtil;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
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

    // [Post] /admin/categories
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto)
    {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setParent(categoryDto.getParent() != null ? categoryDto.getParent() : null);
        category.setDescription(categoryDto.getDescription());

        if (categoryDto.getPosition() == null) {
            Optional<Category> cateWithMaxPosition = categoryRepository.findTopByOrderByPositionDesc();

            int newPosition = cateWithMaxPosition
                    .map(cate -> cate.getPosition() != null ? cate.getPosition() + 1 : 1)
                    .orElse(1);
            category.setPosition(newPosition);
        }
        else {
            category.setPosition(categoryDto.getPosition()); // <-- THIS is crucial
        }

        String baseSlug = SlugUtil.toSlug(categoryDto.getName());
        String uniqueSlug = SlugUtil.generateUniqueSlug(baseSlug, slug -> categoryRepository.existsBySlug(slug));
        category.setSlug(uniqueSlug);

        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.mapToCategoryDto(savedCategory);
    }
}
