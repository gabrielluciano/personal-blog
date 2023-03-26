package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.category.CategoryCreateRequest;
import com.gabrielluciano.blog.dto.category.CategoryResponse;
import com.gabrielluciano.blog.dto.category.CategoryUpdateRequest;
import com.gabrielluciano.blog.mappers.CategoryMapper;
import com.gabrielluciano.blog.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("categories/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        CategoryResponse categoryResponse = CategoryMapper.INSTANCE
                .toCategoryResponse(categoryService.findByIdOrThrowException(id));
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping("categories")
    public ResponseEntity<List<CategoryResponse>> listAll() {
        List<CategoryResponse> categoryResponsesList = categoryService.listAll()
                .stream()
                .map(CategoryMapper.INSTANCE::toCategoryResponse)
                .toList();
        return ResponseEntity.ok(categoryResponsesList);
    }

    @PostMapping("admin/categories")
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryCreateRequest categoryCreateRequest) {
        CategoryResponse categoryResponse = CategoryMapper.INSTANCE
                .toCategoryResponse(categoryService.create(categoryCreateRequest));
        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }

    @PutMapping("admin/categories")
    public ResponseEntity<Void> update(@RequestBody CategoryUpdateRequest categoryUpdateRequest) {
        categoryService.update(categoryUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("admin/categories/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
