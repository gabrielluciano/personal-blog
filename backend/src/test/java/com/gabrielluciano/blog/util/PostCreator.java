package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.models.Tag;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

public class PostCreator {

    private static final LocalDateTime date = LocalDateTime.now(ZoneOffset.UTC);

    private static final Post post = Post.builder()
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
        post.setPublished(true);
        post.setCreatedAt(date);
        post.setUpdatedAt(date);
        post.setPublishedAt(date);
        post.setTags(null);
        return post;
    }

    public static Post createUnpublishedPost() {
        post.setPublished(false);
        post.setCreatedAt(date);
        post.setUpdatedAt(date);
        post.setPublishedAt(null);
        post.setTags(null);
        return post;
    }

    public static Post createPublishedPostWithTags(Set<Tag> tags) {
        post.setPublished(true);
        post.setCreatedAt(date);
        post.setUpdatedAt(date);
        post.setPublishedAt(date);
        post.setTags(tags);
        return post;
    }
}
