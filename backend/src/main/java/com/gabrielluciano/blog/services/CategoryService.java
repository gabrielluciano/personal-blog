package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.error.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.models.entities.Category;
import com.gabrielluciano.blog.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, id));
    }

    public List<Category> findCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category, Long id) {
        Category categoryFromDb = findCategoryById(id);
        categoryFromDb.setName(category.getName());
        categoryFromDb.setDescription(category.getDescription());
        categoryFromDb.setSlug(category.getSlug());
        return categoryRepository.save(categoryFromDb);
    }

    public void deleteCategoryById(Long id) {
        Category categoryFromDb = findCategoryById(id);
        categoryRepository.deleteById(id);
    }
}
