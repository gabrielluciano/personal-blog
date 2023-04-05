package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {

    Page<Tag> list(Pageable pageable);

    Tag findByIdOrThrowResourceNotFoundException(long id) throws ResourceNotFoundException;

    Tag save(TagCreateRequest tagCreateRequest);

    void update(TagUpdateRequest tagUpdateRequest, long id);

    void deleteById(long id);
}
