package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.CreatePostCommentRequest;
import com.gabrielluciano.blog.dto.PostCommentResponse;
import com.gabrielluciano.blog.models.entities.PostComment;
import com.gabrielluciano.blog.security.models.SecurityUser;
import com.gabrielluciano.blog.services.PostCommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class PostCommentController {

    private PostCommentService service;

    @GetMapping("posts/{postId}/comments")
    public ResponseEntity<Page<PostCommentResponse>> getPostCommentsByPostId(@PathVariable Long postId,
                                                                         Pageable pageable) {
        return new ResponseEntity<>(service.findPostCommentsOfPublishedPost(postId, pageable), HttpStatus.OK);
    }

    @GetMapping("admin/posts/comments/{status}")
    public ResponseEntity<Page<PostCommentResponse>> getPostCommentsByStatus(@PathVariable String status,
                                                                             Pageable pageable) {
        return new ResponseEntity<>(service.findPostCommentsByStatus(status, pageable), HttpStatus.OK);
    }

    @PostMapping("user/posts/comments")
    public ResponseEntity<PostCommentResponse> createPostComment(@RequestBody CreatePostCommentRequest postComment) {
        return new ResponseEntity<>(service.createPostComment(postComment), HttpStatus.CREATED);
    }
}
