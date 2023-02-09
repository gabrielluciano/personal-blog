package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.entities.Category;
import com.gabrielluciano.blog.models.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findBySlug(String slug);

    Page<Post> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    Page<Post> findAllByPublishedOrderByPublishedAtDesc(Boolean published, Pageable pageable);

    Page<Post> findAllByCategoryAndPublishedIsTrueOrderByPublishedAtDesc(Category category, Pageable pageable);

}
