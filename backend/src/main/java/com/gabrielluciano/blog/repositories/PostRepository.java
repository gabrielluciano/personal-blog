package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByPublishedIsTrueAndTitleContainingIgnoreCaseAndTagsId(String title, Long tagId, Pageable pageable);

    Page<Post> findAllByPublishedIsTrueAndTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Post> findAllByPublishedIsTrueAndTagsId(Long tagId, Pageable pageable);

    Page<Post> findAllByPublishedIsFalse(Pageable pageable);

    Page<Post> findAllByPublishedIsTrue(Pageable pageable);
}
