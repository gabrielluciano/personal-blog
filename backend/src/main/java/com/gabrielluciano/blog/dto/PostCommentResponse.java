package com.gabrielluciano.blog.dto;

import com.gabrielluciano.blog.models.PostCommentStatus;
import com.gabrielluciano.blog.models.entities.PostComment;
import com.gabrielluciano.blog.models.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostCommentResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostCommentStatus status;
    private Long postId;
    private User user;

    public PostCommentResponse(PostComment comment) {
        id = comment.getId();
        content = comment.getContent();
        createdAt = comment.getCreatedAt();
        updatedAt = comment.getUpdatedAt();
        status = comment.getStatus();
        postId = comment.getPost().getId();
        user = comment.getUser();
    }
}
