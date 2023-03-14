package com.gabrielluciano.blog.dto;

import com.gabrielluciano.blog.models.entities.PostComment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostCommentRequest {

    public String content;

    public PostComment toNewPostComment() {
        return new PostComment(content);
    }

}
