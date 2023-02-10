package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.entities.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByIdAndDeletedAtNull(Long id);

    Page<Tag> findAllByDeletedAtNull(Pageable pageable);
}
