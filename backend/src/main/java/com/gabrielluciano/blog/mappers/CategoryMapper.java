package com.gabrielluciano.blog.mappers;

import com.gabrielluciano.blog.dto.category.CategoryCreateRequest;
import com.gabrielluciano.blog.dto.category.CategoryResponse;
import com.gabrielluciano.blog.dto.category.CategoryUpdateRequest;
import com.gabrielluciano.blog.models.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toCategory(CategoryCreateRequest categoryCreateRequest);

    void updateCategoryFromCategoryUpdateRequest(CategoryUpdateRequest categoryUpdateRequest, @MappingTarget Category category);

    CategoryResponse toCategoryResponse(Category category);
}
