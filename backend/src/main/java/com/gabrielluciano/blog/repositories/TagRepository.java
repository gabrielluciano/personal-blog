package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
