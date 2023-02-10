package com.gabrielluciano.blog.exceptions;

public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(Long id) {
        super("Could not find tag with id " + id);
    }
}
