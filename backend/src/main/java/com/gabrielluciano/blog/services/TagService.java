package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {

    Page<Tag> list(Pageable pageable);

    Tag findById(long id);
}
