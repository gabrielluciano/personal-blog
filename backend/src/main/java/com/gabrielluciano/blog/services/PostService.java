package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.CreateAndUpdatePostRequest;
import com.gabrielluciano.blog.dto.MultiPostResponse;
import com.gabrielluciano.blog.error.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.error.exceptions.UserNotAllowedToModifyResourceException;
import com.gabrielluciano.blog.models.entities.Category;
import com.gabrielluciano.blog.models.entities.Post;
import com.gabrielluciano.blog.models.entities.User;
import com.gabrielluciano.blog.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    public Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Post.class, id));
    }

    public Post findPostBySlug(String slug) {
        return postRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(Post.class, slug));
    }

    public Page<MultiPostResponse> findPostsPaginated(Boolean published, Pageable pageable) {
        if (published == null) {
            return postRepository.findAllByOrderByUpdatedAtDesc(pageable)
                    .map(MultiPostResponse::new);
        } else {
            return postRepository.findAllByPublishedOrderByPublishedAtDesc(published, pageable)
                    .map(MultiPostResponse::new);
        }
    }

    public Page<MultiPostResponse> findPublishedPostsByCategoryPaginated(Long categoryId, Pageable pageable) {
        Category category = categoryService.findByIdOrThrowError(categoryId);
        return postRepository.findAllByCategoryAndPublishedIsTrueOrderByPublishedAtDesc(category, pageable)
                .map(post -> new MultiPostResponse(post));
    }

    public Post createPost(CreateAndUpdatePostRequest createPostRequest, User user) {
        Category category = categoryService.findByIdOrThrowError(createPostRequest.getCategoryId());

        Post post = createPostRequest.toNewPost();
        post.setAuthor(user);
        post.setCategory(category);
        updatePostTags(post, createPostRequest.getTagsIds());

        return postRepository.save(post);
    }

    public Post updatePost(CreateAndUpdatePostRequest updatePostRequest, Long id, User user) {
        Post post = findPostById(id);

        verifyIfUserIsAllowedToModifyPost(user, post);

        Category category = categoryService.findByIdOrThrowError(updatePostRequest.getCategoryId());
        updatePostRequest.updatePostContent(post);
        post.setCategory(category);
        updatePostTags(post, updatePostRequest.getTagsIds());

        return postRepository.save(post);
    }

    public void deletePostById(Long id, User user) {
        Post post = findPostById(id);
        verifyIfUserIsAllowedToModifyPost(user, post);
        postRepository.deleteById(post.getId());
    }

    public boolean publishPost(Long id, User user) {
        Post post = findPostById(id);
        verifyIfUserIsAllowedToModifyPost(user, post);
        if (!post.getPublished()) {
            post.setPublished(true);
            post.setPublishedAt(LocalDateTime.now(ZoneOffset.UTC));
            postRepository.save(post);
            return true;
        }
        return false;
    }

    public boolean unpublishPost(Long id, User user) {
        Post post = findPostById(id);
        verifyIfUserIsAllowedToModifyPost(user, post);
        if (post.getPublished()) {
            post.setPublished(false);
            post.setPublishedAt(null);
            postRepository.save(post);
            return true;
        }
        return false;
    }

    private void updatePostTags(Post post, Long[] tagsIds) {
        post.getTags().clear();
        Arrays.stream(tagsIds).forEach(tagId -> tagService.findOptionalById(tagId)
                .ifPresent(post::addTag));
    }

    private void verifyIfUserIsAllowedToModifyPost(User user, Post post) {
        if (user.isNotAdmin() && !post.isUserAuthorOfThisPost(user)) {
            throw new UserNotAllowedToModifyResourceException(user.getId());
        }
    }
}
