package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.models.Post;

public class PostCreateRequestCreator {

    private static final Post post = PostCreator.createPublishedPost();

    private PostCreateRequestCreator() {
    }

    public static PostCreateRequest createValidPostCreateRequest() {
        return createPostCreateRequestWithTitle(post.getTitle());
    }

    public static PostCreateRequest createPostCreateRequestWithTitle(String title) {
        return createPostCreateRequestWithTitleAndSlug(title, post.getSlug());
    }

    public static PostCreateRequest createPostCreateRequestWithSlug(String slug) {
        return createPostCreateRequestWithTitleAndSlug(post.getTitle(), slug);
    }

    private static PostCreateRequest createPostCreateRequestWithTitleAndSlug(String title, String slug) {
        return PostCreateRequest.builder()
                .title(title)
                .slug(slug)
                .subtitle(post.getSubtitle())
                .content(post.getContent())
                .metaTitle(post.getMetaTitle())
                .metaDescription(post.getMetaDescription())
                .imageUrl(post.getImageUrl())
                .build();
    }
}
