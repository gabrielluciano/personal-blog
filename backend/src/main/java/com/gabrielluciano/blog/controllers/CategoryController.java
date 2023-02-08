package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.models.entities.Category;
import com.gabrielluciano.blog.services.CategoryService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return service.findCategoryById(id);
    }

    @GetMapping
    public Iterable<Category> getCategories() {
        return service.findCategories();
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return service.createCategory(category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@RequestBody Category category, @PathVariable Long id) {
        return service.updateCategory(category, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        service.deleteCategoryById(id);
    }
}
