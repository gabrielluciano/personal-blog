package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findBySlug(String slug);

}
