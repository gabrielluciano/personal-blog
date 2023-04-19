package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.mappers.PostMapper;
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
        return postRepository.findAll(pageable)
                .map(PostMapper.INSTANCE::postToPostResponse);
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
}
