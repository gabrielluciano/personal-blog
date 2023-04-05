package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.models.Tag;

public class TagCreator {

    private static final Tag tag = Tag.builder()
            .id(1L)
            .name("News")
            .description("Get the latest news")
            .slug("news")
            .build();

    private TagCreator() {
    }

    public static Tag createValidTag() {
        return tag;
    }
}
