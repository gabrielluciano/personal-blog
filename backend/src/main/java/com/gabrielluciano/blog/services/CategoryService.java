package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.category.CategoryCreateRequest;
import com.gabrielluciano.blog.dto.category.CategoryUpdateRequest;
import com.gabrielluciano.blog.error.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.mappers.CategoryMapper;
import com.gabrielluciano.blog.models.entities.Category;
import com.gabrielluciano.blog.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findByIdOrThrowException(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, id));
    }

    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    public Category create(CategoryCreateRequest categoryCreateRequest) {
        Category category = CategoryMapper.INSTANCE.toCategory(categoryCreateRequest);
        return categoryRepository.save(category);
    }

    public void update(CategoryUpdateRequest categoryUpdateRequest) {
        Category category = findByIdOrThrowException(categoryUpdateRequest.getId());
        CategoryMapper.INSTANCE.updateCategoryFromCategoryUpdateRequest(categoryUpdateRequest, category);
        categoryRepository.save(category);
    }

    public void deleteById(Long id) {
        findByIdOrThrowException(id);
        categoryRepository.deleteById(id);
    }
}
