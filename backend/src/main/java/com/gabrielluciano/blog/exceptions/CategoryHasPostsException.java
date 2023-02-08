package com.gabrielluciano.blog.exceptions;

public class CategoryHasPostsException extends RuntimeException {
    public CategoryHasPostsException(Long id) {
        super("Category with id " + id + " cannot be deleted because it has posts associated with it");
    }
}
