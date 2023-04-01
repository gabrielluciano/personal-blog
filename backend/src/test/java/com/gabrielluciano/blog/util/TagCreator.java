package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.models.Tag;

public class TagCreator {

    private TagCreator() {
    }

    public static Tag createValidTag() {
        Tag tag = Tag.builder()
                .id(1L)
                .name("News")
                .description("Get the latest news")
                .slug("news")
                .build();

        tag.setUuid("uuid"); // Overrides the random uuid generated when tag is created

        return tag;
    }

}
