package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.models.Tag;

public class TagCreator {

    private static final Tag tagToBeSaved = Tag.builder()
            .name("News")
            .description("Get the latest news")
            .slug("news")
            .build();

    private static final Tag validTag = Tag.builder()
            .id(1L)
            .name("News")
            .description("Get the latest news")
            .slug("news")
            .build();

    private TagCreator() {
    }

    public static Tag createValidTag() {
        return validTag;
    }

    public static Tag createTagToBeSaved() {
        return tagToBeSaved;
    }
}
