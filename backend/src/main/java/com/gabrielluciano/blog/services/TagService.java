package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {

    Page<TagResponse> list(Pageable pageable);

    TagResponse findById(long id) throws ResourceNotFoundException;

    TagResponse save(TagCreateRequest tagCreateRequest);

    void update(TagUpdateRequest tagUpdateRequest, long id);

    void deleteById(long id);
}
