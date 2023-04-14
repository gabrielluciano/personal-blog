package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.mappers.PostMapper;
import lombok.Data;

@Data
public class PostResponseCreator {

    private PostResponseCreator() {
    }

    public static PostResponse createPublishedPostResponse() {
        return PostMapper.INSTANCE.postToPostResponse(PostCreator.createPublishedPost());
    }

    public static PostResponse createUnpublishedPostResponse() {
        return PostMapper.INSTANCE.postToPostResponse(PostCreator.createUnpublishedPost());
    }
}
