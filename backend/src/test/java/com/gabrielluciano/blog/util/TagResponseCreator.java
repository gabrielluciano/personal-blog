package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.mappers.TagMapper;

public class TagResponseCreator {

    private TagResponseCreator() {
    }

    public static TagResponse createValidTagResponse() {
        return TagMapper.INSTANCE.tagToTagResponse(TagCreator.createValidTag());
    }
}
