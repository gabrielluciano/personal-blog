package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    
    @Override
    public Page<PostResponse> list(Pageable pageable) {
        return null;
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
}
