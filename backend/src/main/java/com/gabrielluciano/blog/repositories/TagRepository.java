package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.entities.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Long> {
}
