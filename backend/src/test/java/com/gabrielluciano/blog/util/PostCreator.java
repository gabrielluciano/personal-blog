package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.models.Post;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class PostCreator {

    private static final LocalDateTime date = LocalDateTime.now(ZoneOffset.UTC);

    private static final Post publishedPost = Post.builder()
            .id(1L)
            .title("Some Post")
            .subtitle("Some Post with awesome content")
            .content("This is the post content")
            .metaTitle("Some Post")
            .metaDescription("Some Post with awesome content")
            .slug("some-post")
            .imageUrl("https://example.com")
            .published(true)
            .createdAt(date)
            .updatedAt(date)
            .publishedAt(date)
            .build();

    private PostCreator() {
    }

    public static Post createPublishedPost() {
        return publishedPost;
    }
}
