package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.models.Post;

public class PostCreateRequestCreator {

    private PostCreateRequestCreator() {
    }

    public static PostCreateRequest createValidPostCreateRequest() {
        Post post = PostCreator.createPublishedPost();

        return PostCreateRequest.builder()
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
