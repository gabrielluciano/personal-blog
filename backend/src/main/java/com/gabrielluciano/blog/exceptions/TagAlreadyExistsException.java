package com.gabrielluciano.blog.exceptions;

import com.gabrielluciano.blog.models.entities.Tag;

public class TagAlreadyExistsException extends RuntimeException {

    public TagAlreadyExistsException(Tag tag) {
        super("A tag with name " + tag.getName() + " or slug " + tag.getSlug() + " already exists");
    }
}
