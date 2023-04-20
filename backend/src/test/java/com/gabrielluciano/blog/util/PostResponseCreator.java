package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.mappers.PostMapper;
import com.gabrielluciano.blog.models.Tag;

import java.util.Set;

public class PostResponseCreator {

    private PostResponseCreator() {
    }

    public static PostResponse createPublishedPostResponse() {
        return PostMapper.INSTANCE.postToPostResponse(PostCreator.createPublishedPost());
    }

    public static PostResponse createUnpublishedPostResponse() {
        return PostMapper.INSTANCE.postToPostResponse(PostCreator.createUnpublishedPost());
    }

    public static PostResponse createPublishedPostResponseWithTitleAndSlug(String title, String slug) {
        return PostMapper.INSTANCE.postToPostResponse(PostCreator.createPublishedPostWithTitleAndSlug(title, slug));
    }

    public static PostResponse createPublishedPostResponseWithTags(Set<Tag> tags) {
        return PostMapper.INSTANCE.postToPostResponse(PostCreator.createPublishedPostWithTags(tags));
    }
}
