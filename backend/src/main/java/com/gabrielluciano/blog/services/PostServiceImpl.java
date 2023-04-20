package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
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
        if (drafts) {
            return getPageOfUnpublishedPostResponses(pageable);
        }

        return getPageOfPublishedPostResponsesFilteredByTitle(pageable, title);
    }

    @Override
    public PostResponse findById(long id) {
        return null;
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

    private Page<PostResponse> getPageOfUnpublishedPostResponses(Pageable pageable) {
        return postRepository.findAllByPublishedIsFalse(pageable)
                .map(PostMapper.INSTANCE::postToPostResponse);
    }

    private Page<PostResponse> getPageOfPublishedPostResponsesFilteredByTitle(Pageable pageable, String title) {
        Page<Post> posts;
        if (title == null) {
            posts = postRepository.findAllByPublishedIsTrue(pageable);
        } else {
            posts = postRepository.findByPublishedIsTrueAndTitleContainingIgnoreCase(title, pageable);
        }
        return posts.map(PostMapper.INSTANCE::postToPostResponse);
    }
}
