package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.error.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.error.exceptions.UserNotAllowedToModifyResourceException;
import com.gabrielluciano.blog.mappers.PostMapper;
import com.gabrielluciano.blog.models.entities.Category;
import com.gabrielluciano.blog.models.entities.Post;
import com.gabrielluciano.blog.models.entities.Tag;
import com.gabrielluciano.blog.models.entities.User;
import com.gabrielluciano.blog.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    public Post findByIdOrThrowException(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Post.class, id));
    }

    public Post findPublishedPostByIdOrThrowException(Long id) {
        return postRepository.findByIdAndPublishedIsTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(Post.class, id));
    }

    public Post findPublishedPostBySlugOrThrowException(String slug) {
        return postRepository.findBySlugIgnoringCaseAndPublishedIsTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException(Post.class, slug));
    }

    public Page<Post> listPublishedPosts(Pageable pageable) {
        return postRepository.findAllByPublishedIsTrueOrderByPublishedAtDesc(pageable);
    }

    public Page<Post> list(Pageable pageable) {
        return postRepository.findAllByOrderByPublishedAtDesc(pageable);
    }

    public Page<Post> listPublishedPostsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryService.findByIdOrThrowException(categoryId);
        return postRepository.findAllByCategoryAndPublishedIsTrueOrderByPublishedAtDesc(category, pageable);
    }

    public Post create(PostCreateRequest postCreateRequest, User user) {
        Category category = categoryService.findByIdOrThrowException(postCreateRequest.getCategoryId());

        Post post = PostMapper.INSTANCE.toPost(postCreateRequest);
        post.setAuthor(user);
        post.setCategory(category);
        post.setPublished(false);

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        return postRepository.save(post);
    }

    public void update(PostUpdateRequest postUpdateRequest, User user) {
        Post post = findByIdOrThrowException(postUpdateRequest.getId());
        Category category = categoryService.findByIdOrThrowException(postUpdateRequest.getCategoryId());

        throwExceptionIfUserIsNotAllowedToModifyPost(user, post);

        PostMapper.INSTANCE.updatePostFromPostUpdateRequest(postUpdateRequest, post);

        post.setCategory(category);
        post.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));

        postRepository.save(post);
    }

    public void deleteById(Long id, User user) {
        Post post = findByIdOrThrowException(id);
        throwExceptionIfUserIsNotAllowedToModifyPost(user, post);
        postRepository.deleteById(post.getId());
    }

    public boolean publishById(Long id, User user) {
        Post post = findByIdOrThrowException(id);
        throwExceptionIfUserIsNotAllowedToModifyPost(user, post);
        if (post.getPublished().equals(true)) {
            return false;
        }
        post.setPublished(true);
        post.setPublishedAt(LocalDateTime.now(ZoneOffset.UTC));
        postRepository.save(post);
        return true;
    }

    public boolean unpublishById(Long id, User user) {
        Post post = findByIdOrThrowException(id);
        throwExceptionIfUserIsNotAllowedToModifyPost(user, post);
        if (post.getPublished().equals(false)) {
            return false;
        }
        post.setPublished(false);
        post.setPublishedAt(null);
        postRepository.save(post);
        return true;
    }

    @Transactional
    public void updateTags(Long postId, Long[] tagIds, User user) {
        Post post = findByIdOrThrowException(postId);
        throwExceptionIfUserIsNotAllowedToModifyPost(user, post);

        post.getTags().clear();

        Arrays.stream(tagIds).forEach(tagId -> {
            Tag tag = tagService.findByIdOrThrowException(tagId);
            post.addTag(tag);
        });

        postRepository.save(post);
    }

    private void throwExceptionIfUserIsNotAllowedToModifyPost(User user, Post post) {
        if (user.isNotAdmin() && !post.isUserAuthorOfThisPost(user)) {
            throw new UserNotAllowedToModifyResourceException(user.getId());
        }
    }
}
