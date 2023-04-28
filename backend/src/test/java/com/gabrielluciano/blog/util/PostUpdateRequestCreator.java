package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.models.Post;

public class PostUpdateRequestCreator {

    private static final Post post = PostCreator.createPublishedPost();

    private PostUpdateRequestCreator() {
    }

    public static PostUpdateRequest createValidPostUpdateRequest() {
        return createValidPostUpdateRequestWithTitleAndSlug(post.getTitle(), post.getSlug());
    }

    public static PostUpdateRequest createValidPostUpdateRequestWithTitle(String title) {
        return createValidPostUpdateRequestWithTitleAndSlug(title, post.getSlug());
    }

    public static PostUpdateRequest createValidPostUpdateRequestWithSlug(String slug) {
        return createValidPostUpdateRequestWithTitleAndSlug(post.getTitle(), slug);
    }

    public static PostUpdateRequest createValidPostUpdateRequestWithTitleAndSlug(String title, String slug) {
        return PostUpdateRequest.builder()
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
