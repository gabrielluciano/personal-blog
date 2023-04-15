package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.models.Post;

public class PostUpdateRequestCreator {

    private PostUpdateRequestCreator() {
    }

    public static PostUpdateRequest createValidPostUpdateRequest() {
        Post post = PostCreator.createPublishedPost();

        return PostUpdateRequest.builder()
                .title(post.getTitle())
                .subtitle(post.getSubtitle())
                .content(post.getContent())
                .metaTitle(post.getMetaTitle())
                .metaDescription(post.getMetaDescription())
                .slug(post.getSlug())
                .imageUrl(post.getImageUrl())
                .build();
    }
}
