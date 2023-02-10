package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.models.entities.Tag;
import com.gabrielluciano.blog.services.TagService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id) {
        return service.findTagById(id);
    }

    @GetMapping
    public Page<Tag> getTagsPaginated(
            @RequestParam(defaultValue = "0", name = "offset") Integer page,
            @RequestParam(defaultValue = "10", name = "limit") Integer size) {

        return service.findTagsPaginated(page, size);
    }

    @PostMapping
    public Tag createTag(@RequestBody Tag tag) {
        return service.createTag(tag);
    }

    @PutMapping("/{id}")
    public Tag updateTag(@RequestBody Tag tag, @PathVariable Long id) {
        return service.updateTag(tag, id);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        service.deleteTagById(id);
    }
}
