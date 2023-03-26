package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.CreateAndUpdatePostCommentRequest;
import com.gabrielluciano.blog.dto.PostCommentResponse;
import com.gabrielluciano.blog.error.exceptions.InvalidPostCommentStatusException;
import com.gabrielluciano.blog.error.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.error.exceptions.UserNotAllowedToModifyResourceException;
import com.gabrielluciano.blog.models.PostCommentStatus;
import com.gabrielluciano.blog.models.entities.Post;
import com.gabrielluciano.blog.models.entities.PostComment;
import com.gabrielluciano.blog.models.entities.User;
import com.gabrielluciano.blog.repositories.PostCommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@AllArgsConstructor
public class PostCommentService {

    private PostCommentRepository postCommentRepository;
    private PostService postService;

    public Page<PostCommentResponse> findPostCommentsOfPublishedPost(Long postId, Pageable pageable) {
        Post post = postService.findByIdOrThrowException(postId);

        if (post.getPublished()) {
            return postCommentRepository.findAllByPostAndStatusOrderByCreatedAtDesc(post,
                    PostCommentStatus.APPROVED, pageable).map(PostCommentResponse::new);
        }

        return Page.empty();
    }

    public Page<PostCommentResponse> findPostCommentsByStatus(String status, Pageable pageable) {
        PostCommentStatus postCommentStatus = mapStringToPostCommentStatus(status);
        return postCommentRepository.findAllByStatusOrderByCreatedAtDesc(postCommentStatus, pageable)
                .map(PostCommentResponse::new);
    }

    public PostCommentResponse createPostComment(CreateAndUpdatePostCommentRequest postCommentRequest,
                                                 Long postId, User user) {
        Post post = postService.findByIdOrThrowException(postId);
        PostComment postComment = postCommentRequest.toNewPostComment();
        postComment.setPost(post);
        postComment.setUser(user);
        return new PostCommentResponse(postCommentRepository.save(postComment));
    }

    public PostCommentResponse updatePostComment(CreateAndUpdatePostCommentRequest postCommentRequest,
                                                 Long id,
                                                 User user) {
        PostComment postComment = findPostCommentById(id);
        verifyIfUserIsAllowedToModifyPostComment(user, postComment);

        postComment.setContent(postCommentRequest.getContent());
        postComment.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        postComment.setStatus(PostCommentStatus.WAITING_FOR_APPROVAL);
        return new PostCommentResponse(postCommentRepository.save(postComment));
    }

    public void deletePostCommentById(Long id, User user) {
        PostComment postComment = findPostCommentById(id);
        verifyIfUserIsAllowedToModifyPostComment(user, postComment);
        postCommentRepository.deleteById(id);
    }

    public void updatePostCommentStatus(Long id, String status) {
        PostComment postComment = findPostCommentById(id);
        PostCommentStatus newStatus = mapStringToPostCommentStatus(status);
        postComment.setStatus(newStatus);
        postCommentRepository.save(postComment);
    }

    private PostCommentStatus mapStringToPostCommentStatus(String status) {
        if (status.equalsIgnoreCase("WAITING")) {
             return PostCommentStatus.WAITING_FOR_APPROVAL;
        } else if (status.equalsIgnoreCase("APPROVED")) {
            return PostCommentStatus.APPROVED;
        } else if (status.equalsIgnoreCase("REJECTED")) {
            return PostCommentStatus.REJECTED;
        } else {
            throw new InvalidPostCommentStatusException();
        }
    }

    private PostComment findPostCommentById(Long id) {
        return postCommentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PostComment.class, id));
    }

    private void verifyIfUserIsAllowedToModifyPostComment(User user, PostComment postComment) {
        if (!postComment.getUser().equals(user)) {
            throw new UserNotAllowedToModifyResourceException(user.getId());
        }
    }
}
