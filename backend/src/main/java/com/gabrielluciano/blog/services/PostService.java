package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.PostRequestDTO;
import com.gabrielluciano.blog.exceptions.CategoryNotFoundException;
import com.gabrielluciano.blog.exceptions.PaginationException;
import com.gabrielluciano.blog.exceptions.PostNotFoundException;
import com.gabrielluciano.blog.exceptions.UserNotFoundException;
import com.gabrielluciano.blog.models.entities.Category;
import com.gabrielluciano.blog.models.entities.Post;
import com.gabrielluciano.blog.models.entities.Tag;
import com.gabrielluciano.blog.models.entities.User;
import com.gabrielluciano.blog.repositories.CategoryRepository;
import com.gabrielluciano.blog.repositories.PostRepository;
import com.gabrielluciano.blog.repositories.TagRepository;
import com.gabrielluciano.blog.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    public Post findPostBySlug(String slug) {
        return postRepository.findBySlug(slug)
                .orElseThrow(() -> new PostNotFoundException(slug));
    }

    public Page<Post> findPostsPaginated(Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return postRepository.findAll(pageable);
        } catch (IllegalArgumentException ex) {
            throw new PaginationException(page, size);
        }
    }

    public Post createPost(PostRequestDTO postRequestDTO) {
        Post newPost = postRequestDTO.convertToPost();

        User user = userRepository.findById(postRequestDTO.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException(postRequestDTO.getAuthorId()));

        Category category = categoryRepository.findById(postRequestDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(postRequestDTO.getCategoryId()));

        newPost.setAuthor(user);
        newPost.setCategory(category);

        for(Long tagId : postRequestDTO.getTagsIds()) {
            Optional<Tag> tag = tagRepository.findById(tagId);
            if (tag.isPresent()) {
                newPost.addTag(tag.get());
            }
        }

        return postRepository.save(newPost);
    }
}
