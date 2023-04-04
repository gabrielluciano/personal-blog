package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.models.Tag;

public class TagUpdateRequestCreator {

    private TagUpdateRequestCreator() {
    }

    public static TagUpdateRequest createValidTagUpdateRequest() {
        Tag tag = TagCreator.createValidTag();

        return TagUpdateRequest.builder()
                .name(tag.getName())
                .slug(tag.getSlug())
                .description(tag.getDescription())
                .build();
    }
}
