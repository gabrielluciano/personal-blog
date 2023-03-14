package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.PostCommentStatus;
import com.gabrielluciano.blog.models.entities.Post;
import com.gabrielluciano.blog.models.entities.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Page<PostComment> findAllByStatusOrderByCreatedAtDesc(PostCommentStatus status, Pageable pageable);

    Page<PostComment> findAllByPostAndStatusOrderByCreatedAtDesc(Post post, PostCommentStatus status, Pageable pageable);

}
