package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("posts/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @GetMapping("posts/slug/{slug}")
    public ResponseEntity<PostResponse> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(postService.findBySlug(slug));
    }

    @PostMapping("posts")
    public ResponseEntity<PostResponse> save(@RequestBody @Valid PostCreateRequest postCreateRequest) {
        return new ResponseEntity<>(postService.save(postCreateRequest), HttpStatus.CREATED);
    }

    @PutMapping("posts/{id}")
    public ResponseEntity<Void> update(@RequestBody @Valid PostUpdateRequest postUpdateRequest, @PathVariable long id) {
        postService.update(postUpdateRequest, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("posts/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("posts/{postId}/tags/{tagId}")
    public ResponseEntity<Void> addTag(@PathVariable long postId, @PathVariable long tagId) {
        postService.addTag(postId, tagId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("posts/{postId}/tags/{tagId}")
    public ResponseEntity<Void> removeTag(@PathVariable long postId, @PathVariable long tagId) {
        postService.removeTag(postId, tagId);
        return ResponseEntity.noContent().build();
    }
}
