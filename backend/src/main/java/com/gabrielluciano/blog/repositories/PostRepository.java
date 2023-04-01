package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
