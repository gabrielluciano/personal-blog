package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<Page<Tag>> list(Pageable pageable) {
        return ResponseEntity.ok(tagService.list(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> findById(long id) {
        return ResponseEntity.ok(tagService.findById(id));
    }
}
