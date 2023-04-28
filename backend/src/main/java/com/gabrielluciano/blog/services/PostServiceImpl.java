package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.exceptions.ConstraintViolationException;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.mappers.PostMapper;
import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.repositories.PostRepository;
import com.gabrielluciano.blog.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @Override
    public Page<PostResponse> list(Pageable pageable, String title, Long tagId, boolean drafts) {
        Page<Post> posts = getPostPage(pageable, title, tagId, drafts);
        return postPageToPostResponsePage(posts);
    }

    @Override
    public PostResponse findById(long id) {
        Post post = findByIdOrThrowResourceNotFoundException(id);
        return PostMapper.INSTANCE.postToPostResponse(post);
    }

    @Override
    public PostResponse findBySlug(String slug) {
        Post post = findBySlugOrThrowResourceNotFoundException(slug);
        return PostMapper.INSTANCE.postToPostResponse(post);
    }

    @Override
    public PostResponse save(PostCreateRequest postCreateRequest) {
        throwConstraintViolationExceptionIfTitleOrSlugAlreadyExists(postCreateRequest.getTitle(), postCreateRequest.getSlug());
        Post post = PostMapper.INSTANCE.postCreateRequestToPost(postCreateRequest);
        return PostMapper.INSTANCE.postToPostResponse(postRepository.save(post));
    }

    @Override
    public void update(PostUpdateRequest postUpdateRequest, long id) {
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

    private Post findByIdOrThrowResourceNotFoundException(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Post.class, id));
    }

    private Tag findTagByIdOrThrowResourceNotFoundException(long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Tag.class, id));
    }

    private Post findBySlugOrThrowResourceNotFoundException(String slug) {
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

    private Page<PostResponse> postPageToPostResponsePage(Page<Post> posts) {
        return posts.map(PostMapper.INSTANCE::postToPostResponse);
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
}
