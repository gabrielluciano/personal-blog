package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.models.Tag;

public class TagCreator {

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

    public static Tag createNewsTagToBeSaved() {
        return createTagToBeSaved("News");
    }

    public static Tag createTagToBeSaved(String name) {
        return Tag.builder()
                .name(name)
                .slug(name.toLowerCase())
                .description("Some description")
                .build();
    }
}
