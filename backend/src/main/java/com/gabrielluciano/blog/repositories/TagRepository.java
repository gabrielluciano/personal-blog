package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
