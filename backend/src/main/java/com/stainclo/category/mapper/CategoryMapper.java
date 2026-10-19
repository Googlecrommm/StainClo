package com.stainclo.category.mapper;

import com.stainclo.category.dto.request.CategoryRequest;
import com.stainclo.category.dto.response.CategoryResponse;
import com.stainclo.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "categoryStatus", ignore = true)
    Category toEntity(CategoryRequest categoryRequest);
}
