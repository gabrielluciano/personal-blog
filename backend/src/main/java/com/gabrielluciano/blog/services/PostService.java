package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Page<PostResponse> list(Pageable pageable);

    PostResponse findById(long id);

    PostResponse save(PostCreateRequest postCreateRequest);
}
