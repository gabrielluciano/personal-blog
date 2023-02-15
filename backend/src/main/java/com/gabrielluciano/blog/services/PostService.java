package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.MultiplePostsDTO;
import com.gabrielluciano.blog.dto.PostRequestDTO;
import com.gabrielluciano.blog.exceptions.PaginationException;
import com.gabrielluciano.blog.exceptions.PostNotFoundException;
import com.gabrielluciano.blog.exceptions.UserIsNotWriterException;
import com.gabrielluciano.blog.exceptions.UserNotFoundException;
import com.gabrielluciano.blog.models.Role;
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
import java.time.ZoneOffset;
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

    public Page<MultiplePostsDTO> findPostsPaginated(Integer page, Integer size, Boolean published) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            if (published == null) {
                return postRepository.findAllByOrderByUpdatedAtDesc(pageable)
                        .map(post -> new MultiplePostsDTO(post));
            } else {
                return postRepository.findAllByPublishedOrderByPublishedAtDesc(published, pageable)
                        .map(post -> new MultiplePostsDTO(post));
            }
        } catch (IllegalArgumentException ex) {
            throw new PaginationException(page, size);
        }
    }

    public Page<MultiplePostsDTO> findPublishedPostsByCategoryPaginated(Integer page, Integer size, Long categoryId) {
        try {
            Category category = categoryService.findCategoryById(categoryId);
            Pageable pageable = PageRequest.of(page, size);
            return postRepository.findAllByCategoryAndPublishedIsTrueOrderByPublishedAtDesc(category, pageable)
                    .map(post -> new MultiplePostsDTO(post));
        } catch (IllegalArgumentException ex) {
            throw new PaginationException(page, size);
        }
    }

    public Post createPost(PostRequestDTO postRequestDTO) {
        Post newPost = postRequestDTO.toNewPost();

        User user = userRepository.findById(postRequestDTO.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException(postRequestDTO.getAuthorId()));

        if (user.hasRole(Role.WRITER)) {
            throw new UserIsNotWriterException(user.getId());
        }

        Category category = categoryService.findCategoryById(postRequestDTO.getCategoryId());

        newPost.setAuthor(user);
        newPost.setCategory(category);
        updatePostTags(newPost, postRequestDTO.getTagsIds());

        return postRepository.save(newPost);
    }

    public Post updatePost(PostRequestDTO postRequestDTO, Long id) {
        Post post = findPostById(id);
        postRequestDTO.fillPost(post);

        Category category = categoryService.findCategoryById(postRequestDTO.getCategoryId());
        post.setCategory(category);

        updatePostTags(post, postRequestDTO.getTagsIds());
        return postRepository.save(post);
    }

    public void deletePostById(Long id) {
        Post post = findPostById(id);
        postRepository.deleteById(post.getId());
    }

    public boolean publishPost(Long id) {
        Post post = findPostById(id);
        if (!post.isPublished()) {
            post.setPublished(true);
            post.setPublishedAt(LocalDateTime.now(ZoneOffset.UTC));
            postRepository.save(post);
            return true;
        }
        return false;
    }

    public boolean unpublishPost(Long id) {
        Post post = findPostById(id);
        if (post.isPublished()) {
            post.setPublished(false);
            post.setPublishedAt(null);
            postRepository.save(post);
            return true;
        }
        return false;
    }

    private void updatePostTags(Post post, Long[] tagsIds) {
        post.getTags().clear();
        for (Long tagId : tagsIds) {
            Optional<Tag> tag = tagRepository.findById(tagId);
            if (tag.isPresent()) {
                post.addTag(tag.get());
            }
        }
    }
}
