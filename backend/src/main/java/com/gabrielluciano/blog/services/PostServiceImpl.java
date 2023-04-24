package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.mappers.PostMapper;
import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

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
        return null;
    }

    @Override
    public void update(PostUpdateRequest postUpdateRequest, long id) {

    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public void addTag(long postId, long tagId) {

    }

    @Override
    public void removeTag(long postId, long tagId) {

    }

    private Post findByIdOrThrowResourceNotFoundException(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Post.class, id));
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
}
