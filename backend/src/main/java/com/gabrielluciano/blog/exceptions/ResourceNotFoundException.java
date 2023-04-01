package com.gabrielluciano.blog.exceptions;

import com.gabrielluciano.blog.models.AbstractPersistentObject;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Class<? extends AbstractPersistentObject> entity, long id) {
        super("Could not find resource of type " + entity.getSimpleName() + " with id: " + id);
    }

}
