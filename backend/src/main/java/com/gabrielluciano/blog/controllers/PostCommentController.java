package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.CreateAndUpdatePostCommentRequest;
import com.gabrielluciano.blog.dto.PostCommentResponse;
import com.gabrielluciano.blog.security.models.SecurityUser;
import com.gabrielluciano.blog.services.PostCommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("admin/posts/comments/status/{status}")
    public ResponseEntity<Page<PostCommentResponse>> getPostCommentsByStatus(@PathVariable String status,
                                                                             Pageable pageable) {
        return new ResponseEntity<>(service.findPostCommentsByStatus(status, pageable), HttpStatus.OK);
    }

    @PostMapping("user/posts/{postId}/comments")
    public ResponseEntity<PostCommentResponse> createPostComment(@PathVariable Long postId,
                                                                 @RequestBody CreateAndUpdatePostCommentRequest postComment,
                                                                 @AuthenticationPrincipal SecurityUser securityUser) {
        return new ResponseEntity<>(service.createPostComment(postComment, postId, securityUser.getUser()), HttpStatus.CREATED);
    }

    @PutMapping("user/posts/comments/{id}")
    public ResponseEntity<PostCommentResponse> updatePostComment(@PathVariable Long id,
                                                                 @RequestBody CreateAndUpdatePostCommentRequest postComment,
                                                                 @AuthenticationPrincipal SecurityUser securityUser) {
        return new ResponseEntity<>(service.updatePostComment(postComment, id, securityUser.getUser()), HttpStatus.OK);
    }

    @DeleteMapping("user/posts/comments/{id}")
    public ResponseEntity<Void> deletePostComment(@PathVariable Long id,
                                                  @AuthenticationPrincipal SecurityUser securityUser) {
        service.deletePostCommentById(id, securityUser.getUser());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("admin/posts/comments/{id}/status/{status}")
    public ResponseEntity<Void> updatePostCommentStatus(@PathVariable Long id, @PathVariable String status) {
        service.updatePostCommentStatus(id, status);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
