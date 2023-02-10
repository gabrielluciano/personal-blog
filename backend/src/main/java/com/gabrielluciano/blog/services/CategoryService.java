package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.exceptions.CategoryHasPostsException;
import com.gabrielluciano.blog.exceptions.CategoryNotFoundException;
import com.gabrielluciano.blog.models.entities.Category;
import com.gabrielluciano.blog.repositories.CategoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public Iterable<Category> findCategories() {
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

        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new CategoryHasPostsException(id);
        }
    }
}
