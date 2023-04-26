package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("posts")
    public ResponseEntity<Page<PostResponse>> list(Pageable pageable,
                                                   @RequestParam(required = false) String title,
                                                   @RequestParam(required = false, name = "tag") Long tagId,
                                                   @RequestParam(defaultValue = "false") boolean drafts) {
        return ResponseEntity.ok(postService.list(pageable, title, tagId, drafts));
    }

    public ResponseEntity<PostResponse> findById(long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    public ResponseEntity<PostResponse> findBySlug(String slug) {
        return ResponseEntity.ok(postService.findBySlug(slug));
    }

    public ResponseEntity<PostResponse> save(PostCreateRequest postCreateRequest) {
        return new ResponseEntity<>(postService.save(postCreateRequest), HttpStatus.CREATED);
    }

    public ResponseEntity<Void> update(PostUpdateRequest postUpdateRequest, long id) {
        postService.update(postUpdateRequest, id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> deleteById(long id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> addTag(long postId, long tagId) {
        postService.addTag(postId, tagId);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> removeTag(long postId, long tagId) {
        postService.removeTag(postId, tagId);
        return ResponseEntity.noContent().build();
    }
}
