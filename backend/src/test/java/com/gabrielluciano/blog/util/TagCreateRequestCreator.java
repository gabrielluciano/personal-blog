package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.models.Tag;

public class TagCreateRequestCreator {

    private TagCreateRequestCreator() {
    }

    public static TagCreateRequest createValidTagCreateRequest() {
        Tag tag = TagCreator.createValidTag();

        return TagCreateRequest.builder()
                .name(tag.getName())
                .slug(tag.getSlug())
                .description(tag.getDescription())
                .build();
    }
}
