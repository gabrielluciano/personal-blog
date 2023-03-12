package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.models.entities.Tag;
import com.gabrielluciano.blog.services.TagService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TagController {

    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping("tags/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findTagById(id), HttpStatus.OK);
    }

    @GetMapping("tags")
    public ResponseEntity<Page<Tag>> getTagsPaginated(Pageable pageable) {
        return new ResponseEntity<>(service.findTagsPaginated(pageable), HttpStatus.OK);
    }

    @PostMapping("admin/tags")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        return new ResponseEntity<>(service.createTag(tag), HttpStatus.CREATED);
    }

    @PutMapping("admin/tags/{id}")
    public ResponseEntity<Tag> updateTag(@RequestBody Tag tag, @PathVariable Long id) {
        return new ResponseEntity<>(service.updateTag(tag, id), HttpStatus.OK);
    }

    @DeleteMapping("admin/tags/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        service.deleteTagById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
