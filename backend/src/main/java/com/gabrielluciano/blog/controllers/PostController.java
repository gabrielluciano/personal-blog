package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostPagedResponse;
import com.gabrielluciano.blog.dto.post.PostSingleResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.mappers.PostMapper;
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

    private final PostService postService;

    @GetMapping("posts/{id}")
    public ResponseEntity<PostSingleResponse> findPublishedPostById(@PathVariable Long id) {
        PostSingleResponse postSingleResponse = PostMapper.INSTANCE
                .toSinglePostResponse(postService.findPublishedPostByIdOrThrowException(id));
        return ResponseEntity.ok(postSingleResponse);
    }

    @GetMapping("posts/find")
    public ResponseEntity<PostSingleResponse> findPublishedPostBySlug(@RequestParam String slug) {
        PostSingleResponse postSingleResponse = PostMapper.INSTANCE
                .toSinglePostResponse(postService.findPublishedPostBySlugOrThrowException(slug));
        return ResponseEntity.ok(postSingleResponse);
    }

    @GetMapping("posts")
    public ResponseEntity<Page<PostPagedResponse>> listPublishedPosts(Pageable pageable) {
        Page<PostPagedResponse> page = postService.listPublishedPosts(pageable)
                .map(PostMapper.INSTANCE::toPagedPostResponse);
        return ResponseEntity.ok(page);
    }

    @GetMapping("categories/{categoryId}/posts")
    public ResponseEntity<Page<PostPagedResponse>> listPublishedPostsByCategory(
            @PathVariable Long categoryId, Pageable pageable) {

        Page<PostPagedResponse> page = postService.listPublishedPostsByCategory(categoryId, pageable)
                .map(PostMapper.INSTANCE::toPagedPostResponse);
        return ResponseEntity.ok(page);
    }

    @GetMapping("admin/posts/{id}")
    public ResponseEntity<PostSingleResponse> findById(@PathVariable Long id) {
        PostSingleResponse postSingleResponse = PostMapper.INSTANCE
                .toSinglePostResponse(postService.findByIdOrThrowException(id));
        return ResponseEntity.ok(postSingleResponse);
    }

    @GetMapping("admin/posts")
    public ResponseEntity<Page<PostPagedResponse>> list(Pageable pageable) {
        Page<PostPagedResponse> page = postService.list(pageable)
                .map(PostMapper.INSTANCE::toPagedPostResponse);
        return ResponseEntity.ok(page);
    }

    @PostMapping("author/posts")
    public ResponseEntity<PostSingleResponse> create(@RequestBody PostCreateRequest postCreateRequest,
                                                     @AuthenticationPrincipal SecurityUser securityUser) {
        PostSingleResponse postSingleResponse = PostMapper.INSTANCE
                .toSinglePostResponse(postService.create(postCreateRequest, securityUser.getUser()));
        return new ResponseEntity<>(postSingleResponse, HttpStatus.CREATED);
    }

    @PutMapping("author/posts")
    public ResponseEntity<Void> update(@RequestBody PostUpdateRequest postUpdateRequest,
                                       @AuthenticationPrincipal SecurityUser securityUser) {
        postService.update(postUpdateRequest, securityUser.getUser());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("author/posts/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id,
                                           @AuthenticationPrincipal SecurityUser securityUser) {
        postService.deleteById(id, securityUser.getUser());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("author/posts/{id}/publish")
    public ResponseEntity<Boolean> publish(@PathVariable Long id,
                                           @AuthenticationPrincipal SecurityUser securityUser) {
        return ResponseEntity.ok(postService.publishById(id, securityUser.getUser()));
    }

    @PatchMapping("author/posts/{id}/unpublish")
    public ResponseEntity<Boolean> unpublish(@PathVariable Long id,
                                             @AuthenticationPrincipal SecurityUser securityUser) {
        return ResponseEntity.ok(postService.unpublishById(id, securityUser.getUser()));
    }

    @PatchMapping("author/posts/{id}/tags")
    public ResponseEntity<Void> updateTags(@PathVariable(name = "id") Long postId,
                                           @RequestBody Long[] tagIds,
                                           @AuthenticationPrincipal SecurityUser securityUser) {
        postService.updateTags(postId, tagIds, securityUser.getUser());
        return ResponseEntity.noContent().build();
    }
}
