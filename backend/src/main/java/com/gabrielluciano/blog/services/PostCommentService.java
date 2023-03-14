package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.CreatePostCommentRequest;
import com.gabrielluciano.blog.dto.PostCommentResponse;
import com.gabrielluciano.blog.error.exceptions.InvalidPostCommentStatusException;
import com.gabrielluciano.blog.models.PostCommentStatus;
import com.gabrielluciano.blog.models.entities.Post;
import com.gabrielluciano.blog.models.entities.PostComment;
import com.gabrielluciano.blog.repositories.PostCommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostCommentService {

    private PostCommentRepository postCommentRepository;
    private PostService postService;

    public Page<PostCommentResponse> findPostCommentsOfPublishedPost(Long postId, Pageable pageable) {
        Post post = postService.findPostById(postId);

        if (post.getPublished()) {
            return postCommentRepository.findAllByPostAndStatusOrderByCreatedAtDesc(post,
                    PostCommentStatus.APPROVED, pageable).map(PostCommentResponse::new);
        }

        return Page.empty();
    }

    public Page<PostCommentResponse> findPostCommentsByStatus(String status, Pageable pageable) {
        PostCommentStatus postCommentStatus;

        if (status.equalsIgnoreCase("WAITING")) {
            postCommentStatus = PostCommentStatus.WAITING_FOR_APPROVAL;
        } else if (status.equalsIgnoreCase("APPROVED")) {
            postCommentStatus = PostCommentStatus.APPROVED;
        } else if (status.equalsIgnoreCase("REJECTED")) {
            postCommentStatus = PostCommentStatus.REJECTED;
        } else {
            throw new InvalidPostCommentStatusException();
        }

        return postCommentRepository.findAllByStatusOrderByCreatedAtDesc(postCommentStatus, pageable)
                .map(PostCommentResponse::new);
    }

    public PostCommentResponse createPostComment(CreatePostCommentRequest postCommentRequest) {
        PostComment postComment = postCommentRequest.toNewPostComment();
        // TODO finish this method
        return new PostCommentResponse(postCommentRepository.save(postComment));
    }
}
