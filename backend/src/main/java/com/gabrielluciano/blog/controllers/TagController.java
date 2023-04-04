package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.services.TagService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController("tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<Page<Tag>> list(Pageable pageable) {
        return ResponseEntity.ok(tagService.list(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<Tag> findById(@PathVariable long id) {
        return ResponseEntity.ok(tagService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Tag> save(@RequestBody TagCreateRequest tagCreateRequest) {
        return new ResponseEntity<>(tagService.save(tagCreateRequest), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@RequestBody TagUpdateRequest tagUpdateRequest, @PathVariable long id) {
        tagService.update(tagUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
