package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.PostRequestDTO;
import com.gabrielluciano.blog.models.entities.Post;
import com.gabrielluciano.blog.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping("/id/{id}")
    public Post getPostById(@PathVariable Long id) {
        return service.findPostById(id);
    }

    @GetMapping("/{slug}")
    public Post getPostBySlug(@PathVariable String slug) {
        return service.findPostBySlug(slug);
    }

    @GetMapping
    public Page<Post> getPostsPaginated(
            @RequestParam(defaultValue = "0", name = "offset") Integer page,
            @RequestParam(defaultValue = "10", name = "limit") Integer size) {

        return service.findPostsPaginated(page, size);
    }

    @PostMapping
    public Post createPost(@RequestBody PostRequestDTO post) {
        return service.createPost(post);
    }

    @PutMapping("/{id}")
    public Post updatePost(@RequestBody PostRequestDTO post, @PathVariable Long id) {
        return service.updatePost(post, id);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        service.deletePostById(id);
    }

    @PatchMapping("/{id}/publish")
    public boolean publishPost(@PathVariable Long id) {
        return service.publishPost(id);
    }

    @PatchMapping("/{id}/unpublish")
    public boolean unpublishPost(@PathVariable Long id) {
        return service.unpublishPost(id);
    }
}
