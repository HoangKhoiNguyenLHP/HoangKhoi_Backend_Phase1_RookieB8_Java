package nh.khoi.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import nh.khoi.ecommerce.dto.CategoryDto;
import nh.khoi.ecommerce.dto.ProductDto;
import nh.khoi.ecommerce.entity.Category;
import nh.khoi.ecommerce.entity.Product;
import nh.khoi.ecommerce.exception.BadRequestException;
import nh.khoi.ecommerce.exception.ResourceNotFoundException;
import nh.khoi.ecommerce.mapper.CategoryMapper;
import nh.khoi.ecommerce.mapper.ProductMapper;
import nh.khoi.ecommerce.repository.CategoryRepository;
import nh.khoi.ecommerce.repository.ProductRepository;
import nh.khoi.ecommerce.request.CategoryGetProductsRequest;
import nh.khoi.ecommerce.request.CategoryTreeRequest;
import nh.khoi.ecommerce.response.PaginatedResponse;
import nh.khoi.ecommerce.service.CategoryService;
import nh.khoi.ecommerce.utils.SlugUtil;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService
{
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    // -------------- [] -------------- //
    // [GET] /admin/categories
    @Override
    public PaginatedResponse<CategoryDto> getAllCategories(int page, int limit, String keyword)
    {
        // ----- Pagination ----- //
        Pageable pageable = PageRequest.of(
                page - 1,
                limit,
                Sort.by("position").descending()
        );

        Page<Category> listCategories = categoryRepository.findAllByDeletedFalse(pageable);
        // ----- End Pagination ----- //


        // ----- Search ----- //
        if (keyword != null && !keyword.trim().isEmpty()) {
            String slugifiedKeyword = SlugUtil.toSlug(keyword);
            listCategories = categoryRepository.findBySlugContainingIgnoreCaseAndDeletedFalse(slugifiedKeyword, pageable);
        }
        // ----- End search ----- //


        List<CategoryDto> categoryDtos = listCategories
                .getContent()
                .stream()
                .map((eachCategory) -> CategoryMapper.mapToCategoryDto(eachCategory))
                .collect(Collectors.toList());

        PaginatedResponse<CategoryDto> response = new PaginatedResponse<>(
                listCategories.getNumber() + 1,
                listCategories.getTotalPages(),
                listCategories.getTotalElements(),
                (page - 1) * limit,
                categoryDtos
        );

        return response;
    }

    // [GET] /admin/products/:id
    @Override
    public CategoryDto getCategoryById(UUID categoryId)
    {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with given id: " + categoryId
                ));

        return CategoryMapper.mapToCategoryDto(category);
    }
    // -------------- End [] -------------- //


    // -------------- [] -------------- //
    @Override
    public List<CategoryTreeRequest> buildCategoryTree(List<Category> listCategories, UUID parentId)
    {
        List<CategoryTreeRequest> result = new ArrayList<>();

        for (Category category : listCategories) {
            if (Objects.equals(category.getParent(), parentId)) {
                CategoryTreeRequest node = new CategoryTreeRequest(
                        category.getId(),
                        category.getName(),
                        category.getSlug(),
                        buildCategoryTree(listCategories, category.getId()) // recursive
                );
                result.add(node);
            }
        }

        return result;
    }

    // [GET] /admin/categories/create
    @Override
    public List<CategoryTreeRequest> getCategoryCreatePageData()
    {
        List<Category> listCategories = categoryRepository.findAllByDeletedFalse();
        return buildCategoryTree(listCategories, null); // top-level categories
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
    // -------------- End [] -------------- //


    // -------------- [] -------------- //
    // [PATCH] /admin/categories/:id
    @Override
    public CategoryDto editCategory(Map<String, Object> updateFields, UUID categoryId)
    {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with given id: " + categoryId
                ));

        updateFields.forEach((key, value) -> {
            switch(key) {
                case "name":
                    // ----- Validation manually [PATCH] ----- //
                    if(value != null && value.toString().trim().isEmpty()) {
                        throw new BadRequestException("Category name is required!");
                    }
                    // ----- End validation manually [PATCH] ----- //
                    String name = (String) value;
                    category.setName(name);

                    // update slug
                    String baseSlug = SlugUtil.toSlug(name);
                    String uniqueSlug = SlugUtil.generateUniqueSlug(baseSlug, slug -> categoryRepository.existsBySlug(slug));
                    category.setSlug(uniqueSlug);
                    break;

                case "parent":
                    String parentStr = (String) value;
                    if(parentStr == null || parentStr.isBlank()) {
                        category.setParent(null);
                    }
                    else {
                        try {
                            category.setParent(UUID.fromString(parentStr));
                        }
                        catch (IllegalArgumentException e) {
                            throw new IllegalArgumentException("Invalid UUID format for parent ID!");
                        }
                    }
                    break;

                case "description":
                    category.setDescription((String) value);
                    break;

                case "position":
                    if(value == null || value.toString().isBlank()) {
                        Optional<Category> cateWithMaxPosition = categoryRepository.findTopByOrderByPositionDesc();

                        int newPosition = cateWithMaxPosition
                                .map(item -> item.getPosition() != null ? item.getPosition() + 1 : 1)
                                .orElse(1);
                        category.setPosition(newPosition);
                    }
                    else {
                        try {
                            category.setPosition(Integer.parseInt(value.toString()));
                        }
                        catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Position must be an integer!");
                        }
                    }
                    break;
            }
        });

        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.mapToCategoryDto(updatedCategory);
    }
    // -------------- End [] -------------- //


    // -------------- [] -------------- //
    // [DELETE] /admin/categories/:id
    @Override
    public void deleteCategorySoft(UUID categoryId)
    {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with given id: " + categoryId
                ));

        category.setDeleted(true);

        categoryRepository.save(category);
    }

    // [PATCH] /admin/categories/:id/recover
    @Override
    public void recoverCategory(UUID categoryId)
    {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with given id: " + categoryId
                ));

        category.setDeleted(false);

        categoryRepository.save(category);
    }

    // [DELETE] /admin/categories/:id/permanent
    @Override
    public void deleteCategoryPermanent(UUID categoryId)
    {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with given id: " + categoryId
                ));

        categoryRepository.deleteById(categoryId);
    }
    // -------------- End [] -------------- //


    @Override
    public CategoryGetProductsRequest getProductsByCategorySlug(String slug)
    {
        // Find category by slug
        Category category = categoryRepository
                .findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // Recursive: get all child category IDs
        List<UUID> categoryIds = new ArrayList<>();
        categoryIds.add(category.getId());
        collectChildCategoryIds(category.getId(), categoryIds);

        // Get all products matching category IDs
        List<Product> products = productRepository.findByCategoryIds(categoryIds);
        List<ProductDto> productDtos = products.stream()
                .map(ProductMapper::mapToProductDto)
                .toList();


        // Assemble response DTO
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);

        return new CategoryGetProductsRequest(
                categoryDto,
                productDtos
        );
    }

    private void collectChildCategoryIds(UUID parentId, List<UUID> result)
    {
        List<Category> children = categoryRepository.findByParentAndDeletedFalse(parentId);
        for (Category child : children) {
            result.add(child.getId());
            collectChildCategoryIds(child.getId(), result);
        }
    }
}
