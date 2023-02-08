package com.gabrielluciano.blog.exceptions;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long id) {
        super("Could not find post with id " + id);
    }

    public PostNotFoundException(String slug) {
        super("Could not find post with slug " + slug);
    }
}
