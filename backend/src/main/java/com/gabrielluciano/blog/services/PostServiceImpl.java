package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.exceptions.ConstraintViolationException;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.mappers.PostMapper;
import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.models.User;
import com.gabrielluciano.blog.repositories.PostRepository;
import com.gabrielluciano.blog.repositories.TagRepository;
import com.gabrielluciano.blog.security.models.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @Override
    public Page<PostResponse> list(Pageable pageable, String title, Long tagId, boolean drafts) {
        return getPostPage(pageable, title, tagId, drafts)
                .map(PostMapper.INSTANCE::postToPostResponse);
    }

    @Override
    public PostResponse findBySlug(String slug) {
        User user = getUserFromAuthenticationContext();
        Post post;
        if (user != null && user.getRoles().contains(Role.EDITOR)) {
            post = findPostBySlugOrThrowResourceNotFoundException(slug);
        } else {
            post = findPublishedPostBySlugOrThrowResourceNotFoundException(slug);
        }
        return PostMapper.INSTANCE.postToPostResponse(post);
    }

    @Override
    public PostResponse save(PostCreateRequest postCreateRequest) {
        throwConstraintViolationExceptionIfTitleOrSlugAlreadyExists(postCreateRequest.getTitle(), postCreateRequest.getSlug());
        Post post = PostMapper.INSTANCE.postCreateRequestToPost(postCreateRequest);
        post.setAuthor(getUserFromAuthenticationContext());
        return PostMapper.INSTANCE.postToPostResponse(postRepository.save(post));
    }

    @Override
    public void update(PostUpdateRequest postUpdateRequest, long id) {
        throwConstraintViolationExceptionIfTitleOrSlugAlreadyExists(postUpdateRequest.getTitle(),
                postUpdateRequest.getSlug(), id);
        Post post = findByIdOrThrowResourceNotFoundException(id);
        PostMapper.INSTANCE.updatePostFromPostUpdateRequest(postUpdateRequest, post);
        postRepository.save(post);
    }

    @Override
    public void deleteById(long id) {
        findByIdOrThrowResourceNotFoundException(id);
        postRepository.deleteById(id);
    }

    @Override
    public void addTag(long postId, long tagId) {
        Post post = findByIdOrThrowResourceNotFoundException(postId);
        Tag tag = findTagByIdOrThrowResourceNotFoundException(tagId);
        post.getTags().add(tag);
        postRepository.save(post);
    }

    @Override
    public void removeTag(long postId, long tagId) {
        Post post = findByIdOrThrowResourceNotFoundException(postId);
        Tag tag = findTagByIdOrThrowResourceNotFoundException(tagId);
        post.getTags().remove(tag);
        postRepository.save(post);
    }

    @Override
    public void publishById(long id) {
        Post post = findByIdOrThrowResourceNotFoundException(id);
        if (post.getPublished().equals(false)) {
            post.setPublished(true);
            post.setPublishedAt(LocalDateTime.now(ZoneOffset.UTC));
            postRepository.save(post);
        }
    }

    @Override
    public void unpublishById(long id) {
        Post post = findByIdOrThrowResourceNotFoundException(id);
        if (post.getPublished().equals(true)) {
            post.setPublished(false);
            post.setPublishedAt(null);
            postRepository.save(post);
        }
    }

    private Post findByIdOrThrowResourceNotFoundException(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Post.class, id));
    }

    private Tag findTagByIdOrThrowResourceNotFoundException(long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Tag.class, id));
    }

    private Post findPostBySlugOrThrowResourceNotFoundException(String slug) {
        return postRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(Post.class, slug));
    }

    private Post findPublishedPostBySlugOrThrowResourceNotFoundException(String slug) {
        return postRepository.findByPublishedIsTrueAndSlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(Post.class, slug));
    }

    private Page<Post> getPostPage(Pageable pageable, String title, Long tagId, boolean drafts) {
        if (drafts) {
            return postRepository.findAllByPublishedIsFalse(pageable);
        } else if (title != null && tagId != null) {
            return postRepository.findAllByPublishedIsTrueAndTitleContainingIgnoreCaseAndTagsId(title, tagId, pageable);
        } else if (title != null) {
            return postRepository.findAllByPublishedIsTrueAndTitleContainingIgnoreCase(title, pageable);
        } else if (tagId != null) {
            return postRepository.findAllByPublishedIsTrueAndTagsId(tagId, pageable);
        } else {
            return postRepository.findAllByPublishedIsTrue(pageable);
        }
    }

    private void throwConstraintViolationExceptionIfTitleOrSlugAlreadyExists(String title, String slug) {
        postRepository.findFirstByTitleIgnoreCaseOrSlugIgnoreCase(title, slug)
                .ifPresent(post -> prepareConstraintViolationExceptionAndThrow(post, title, slug));
    }

    private void throwConstraintViolationExceptionIfTitleOrSlugAlreadyExists(String title, String slug, long idToUpdate) {
        postRepository.findFirstByTitleIgnoreCaseOrSlugIgnoreCase(title, slug)
                .ifPresent(post -> {
                    if (post.getId().equals(idToUpdate)) return;
                    prepareConstraintViolationExceptionAndThrow(post, title, slug);
                });
    }

    private void prepareConstraintViolationExceptionAndThrow(Post savedPost, String title, String slug) {
        boolean isTitleViolation = savedPost.getTitle().equalsIgnoreCase(title);
        boolean isSlugViolation = savedPost.getSlug().equalsIgnoreCase(slug);
        Map<String, String> violations = new LinkedHashMap<>();

        if (isTitleViolation) violations.put("title", title);
        if (isSlugViolation) violations.put("slug", slug);

        throw new ConstraintViolationException("posts", violations);
    }

    private User getUserFromAuthenticationContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser securityUser) {
            return securityUser.getUser();
        }
        return null;
    }
}
