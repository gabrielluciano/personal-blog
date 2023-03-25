package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.mappers.TagMapper;
import com.gabrielluciano.blog.models.entities.Tag;
import com.gabrielluciano.blog.services.TagService;
import lombok.AllArgsConstructor;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class TagController {

    private final TagService tagService;

    @GetMapping("tags/{id}")
    public ResponseEntity<TagResponse> findById(@PathVariable Long id) {
        TagResponse tagResponse = TagMapper.INSTANCE
                .toTagResponse(tagService.findByIdOrThrowException(id));
        return ResponseEntity.ok(tagResponse);
    }

    @GetMapping("tags")
    public ResponseEntity<Page<TagResponse>> list(Pageable pageable) {
        Page<TagResponse> page = tagService.list(pageable)
                .map(TagMapper.INSTANCE::toTagResponse);
        return ResponseEntity.ok(page);
    }

    @PostMapping("admin/tags")
    public ResponseEntity<TagResponse> create(@RequestBody TagCreateRequest tagCreateRequest) {
        TagResponse tagResponse = TagMapper.INSTANCE.toTagResponse(tagService.create(tagCreateRequest));
        return new ResponseEntity<>(tagResponse, HttpStatus.CREATED);
    }

    @PutMapping("admin/tags")
    public ResponseEntity<Tag> update(@RequestBody TagUpdateRequest tagUpdateRequest) {
        tagService.update(tagUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("admin/tags/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
