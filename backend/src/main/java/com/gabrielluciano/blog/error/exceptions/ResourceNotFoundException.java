package com.gabrielluciano.blog.error.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Class resource, Long id) {
        super("Could not find resource of type '" + resource.getSimpleName() + "' with identifier '" + id + "'");
    }

}
