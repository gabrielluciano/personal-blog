package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Page<PostResponse> list(Pageable pageable, String title, Long tagId, boolean drafts);

    PostResponse findBySlug(String slug);

    PostResponse save(PostCreateRequest postCreateRequest);

    void update(PostUpdateRequest postUpdateRequest, long id);

    void deleteById(long id);

    void addTag(long postId, long tagId);

    void removeTag(long postId, long tagId);

    void publishById(long id);

    void unpublishById(long id);
}
