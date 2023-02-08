package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.PostRequestDTO;
import com.gabrielluciano.blog.exceptions.PaginationException;
import com.gabrielluciano.blog.exceptions.PostNotFoundException;
import com.gabrielluciano.blog.exceptions.UserIsNotWriterException;
import com.gabrielluciano.blog.exceptions.UserNotFoundException;
import com.gabrielluciano.blog.models.entities.Category;
import com.gabrielluciano.blog.models.entities.Post;
import com.gabrielluciano.blog.models.entities.Tag;
import com.gabrielluciano.blog.models.entities.User;
import com.gabrielluciano.blog.repositories.PostRepository;
import com.gabrielluciano.blog.repositories.TagRepository;
import com.gabrielluciano.blog.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final TagRepository tagRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            CategoryService categoryService,
            TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryService = categoryService;
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
        Post newPost = postRequestDTO.toNewPost();

        User user = userRepository.findById(postRequestDTO.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException(postRequestDTO.getAuthorId()));

        if (user.isNotWriter()) {
            throw new UserIsNotWriterException(user.getId());
        }

        Category category = categoryService.findCategoryById(postRequestDTO.getCategoryId());

        newPost.setAuthor(user);
        newPost.setCategory(category);
        updatePostTags(newPost, postRequestDTO.getTagsIds());

        return postRepository.save(newPost);
    }

    public Post updatePost(PostRequestDTO postRequestDTO, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        postRequestDTO.fillPost(post);
        if (post.isPublished()) {
            post.setPublishedAt(LocalDateTime.now());
        }

        Category category = categoryService.findCategoryById(postRequestDTO.getCategoryId());
        post.setCategory(category);

        updatePostTags(post, postRequestDTO.getTagsIds());

        return postRepository.save(post);
    }

    private void updatePostTags(Post post, Long[] tagsIds) {
        post.getTags().clear();
        for(Long tagId : tagsIds) {
            Optional<Tag> tag = tagRepository.findById(tagId);
            if (tag.isPresent()) {
                post.addTag(tag.get());
            }
        }
    }
}
