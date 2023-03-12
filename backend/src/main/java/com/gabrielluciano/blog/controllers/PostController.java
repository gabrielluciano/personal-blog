package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.MultiPostResponse;
import com.gabrielluciano.blog.dto.CreateAndUpdatePostRequest;
import com.gabrielluciano.blog.models.entities.Post;
import com.gabrielluciano.blog.security.models.SecurityUser;
import com.gabrielluciano.blog.services.PostService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class PostController {

    private final PostService service;

    @GetMapping("posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findPostById(id), HttpStatus.OK);
    }

    @GetMapping("posts/slug/{slug}")
    public ResponseEntity<Post> getPostBySlug(@PathVariable String slug) {
        return new ResponseEntity<>(service.findPostBySlug(slug), HttpStatus.OK);
    }

    @GetMapping("posts")
    public ResponseEntity<Page<MultiPostResponse>> getPostsPaginated(
            @RequestParam(required = false) Boolean published,
            Pageable pageable) {
        return new ResponseEntity<>(service.findPostsPaginated(published, pageable), HttpStatus.OK);
    }

    @GetMapping("categories/{categoryId}/posts")
    public ResponseEntity<Page<MultiPostResponse>> getPublishedPostsByCategoryPaginated(
            @PathVariable Long categoryId,
            Pageable pageable) {
        return new ResponseEntity<>(service.findPublishedPostsByCategoryPaginated(categoryId, pageable),
                HttpStatus.OK);
    }

    @PostMapping("author/posts")
    public ResponseEntity<Post> createPost(@RequestBody CreateAndUpdatePostRequest post,
                                           @AuthenticationPrincipal SecurityUser securityUser) {
        return new ResponseEntity<>(service.createPost(post, securityUser.getUser()), HttpStatus.CREATED);
    }

    @PutMapping("author/posts/{id}")
    public ResponseEntity<Post> updatePost(@RequestBody CreateAndUpdatePostRequest post,
                                           @PathVariable Long id,
                                           @AuthenticationPrincipal SecurityUser securityUser) {
        return new ResponseEntity<>(service.updatePost(post, id, securityUser.getUser()), HttpStatus.OK);
    }

    @DeleteMapping("author/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id,
                                           @AuthenticationPrincipal SecurityUser securityUser) {
        service.deletePostById(id, securityUser.getUser());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("author/posts/{id}/publish")
    public ResponseEntity<Boolean> publishPost(@PathVariable Long id,
                                               @AuthenticationPrincipal SecurityUser securityUser) {
        return new ResponseEntity<>(service.publishPost(id, securityUser.getUser()), HttpStatus.OK);
    }

    @PatchMapping("author/posts/{id}/unpublish")
    public ResponseEntity<Boolean> unpublishPost(@PathVariable Long id,
                                                 @AuthenticationPrincipal SecurityUser securityUser) {
        return new ResponseEntity<>(service.unpublishPost(id, securityUser.getUser()), HttpStatus.OK);
    }
}
