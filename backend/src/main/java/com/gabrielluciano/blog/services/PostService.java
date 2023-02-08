package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.exceptions.PaginationException;
import com.gabrielluciano.blog.exceptions.PostNotFoundException;
import com.gabrielluciano.blog.models.entities.Post;
import com.gabrielluciano.blog.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Post findPostById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    public Post findPostBySlug(String slug) {
        return repository.findBySlug(slug)
                .orElseThrow(() -> new PostNotFoundException(slug));
    }

    public Page<Post> findPostsPaginated(Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return repository.findAll(pageable);
        } catch (IllegalArgumentException ex) {
            throw new PaginationException(page, size);
        }
    }

}
