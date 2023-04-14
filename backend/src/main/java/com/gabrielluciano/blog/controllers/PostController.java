package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    public ResponseEntity<Page<PostResponse>> list(Pageable pageable) {
        return ResponseEntity.ok(postService.list(pageable));
    }

    public ResponseEntity<PostResponse> findById(long id) {
        return ResponseEntity.ok(postService.findById(id));
    }
}
