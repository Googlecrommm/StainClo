package com.stainclo.category.service;

import com.stainclo.category.dto.request.CategoryRequest;
import com.stainclo.category.dto.response.CategoryResponse;
import com.stainclo.category.entity.Category;
import com.stainclo.category.enums.CategoryStatus;
import com.stainclo.category.exception.CategoryExceptions;
import com.stainclo.category.mapper.CategoryMapper;
import com.stainclo.category.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;

    }

    private Category findByCategoryId(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow( () -> new CategoryExceptions.NotFound(categoryId));
    }

    //CREATE
    @Transactional
    public CategoryResponse create(CategoryRequest categoryRequest){
        String trimmedCategoryName = categoryRequest.categoryName().trim();
        CategoryRequest categoryRequestTrimmed = new CategoryRequest(trimmedCategoryName);


        if (categoryRepository.existsByCategoryName(trimmedCategoryName)){
            throw new CategoryExceptions.AlreadyExists(trimmedCategoryName);
        }

        Category categoryRequestToEntity = categoryMapper.toEntity(categoryRequestTrimmed);
        Category savedCategory = categoryRepository.save(categoryRequestToEntity);

        return categoryMapper.toResponse(savedCategory);
    }

    //GET BY ID
    public CategoryResponse getById(Long categoryId){
        Category categoryToFind = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryExceptions.NotFound(categoryId));

        return categoryMapper.toResponse(categoryToFind);
    }

    //PAGINATED READ
    public Page<CategoryResponse> findAll(CategoryStatus categoryStatus, Pageable pageable){
        if (categoryStatus == null){
            categoryStatus = CategoryStatus.ACTIVE;
        }
        return categoryRepository.findByCategoryStatus(categoryStatus, pageable)
                .map(categoryMapper::toResponse);
    }

    //UPDATE
    @Transactional
    public CategoryResponse update(CategoryRequest categoryRequest, Long categoryId){
        Category categoryToUpdate = findByCategoryId(categoryId);
        String trimmedCategoryName = categoryRequest.categoryName().trim();

        if (categoryRepository.existsByCategoryNameAndCategoryIdNot(trimmedCategoryName, categoryId)){
            throw new CategoryExceptions.AlreadyExists(trimmedCategoryName);
        }

        categoryToUpdate.setCategoryName(trimmedCategoryName);

        return categoryMapper.toResponse(categoryToUpdate);
    }

    //ARCHIVE
    @Transactional
    public CategoryResponse archive(Long categoryId){
        Category categoryToArchive = findByCategoryId(categoryId);
        categoryToArchive.setCategoryStatus(CategoryStatus.ARCHIVED);

        return categoryMapper.toResponse(categoryToArchive);
    }

    //RESTORE
    @Transactional
    public CategoryResponse restore(Long categoryId){
        Category categoryToRestore = findByCategoryId(categoryId);
        categoryToRestore.setCategoryStatus(CategoryStatus.ACTIVE);

        return categoryMapper.toResponse(categoryToRestore);
    }
}
