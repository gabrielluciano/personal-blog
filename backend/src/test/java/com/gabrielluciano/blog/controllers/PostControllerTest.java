package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.services.PostService;
import com.gabrielluciano.blog.util.PostResponseCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @BeforeEach
    void setUp() {
        Page<PostResponse> postResponsePage = new PageImpl<>(List.of(PostResponseCreator.createPublishedPostResponse()));

        BDDMockito.when(postService.list(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(postResponsePage);

        BDDMockito.when(postService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(PostResponseCreator.createPublishedPostResponse());
    }

    @Test
    @DisplayName("list returns page of post responses when sucessful")
    void list_ReturnsPageOfPostResponses_WhenSuccessful() {
        PostResponse expectedFirstPostResponse = PostResponseCreator.createPublishedPostResponse();

        ResponseEntity<Page<PostResponse>> responseEntity = postController.list(PageRequest.of(0, 10));

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(responseEntity.getBody().getContent().get(0)).isEqualTo(expectedFirstPostResponse);
    }

    @Test
    @DisplayName("list returns empty page of post responses when no post is found")
    void list_ReturnsEmptyPageOfPostResponses_WhenNoPostIsFound() {
        BDDMockito.when(postService.list(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(Page.empty());

        ResponseEntity<Page<PostResponse>> responseEntity = postController.list(PageRequest.of(0, 10));

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findById returns post response when successful")
    void findById_ReturnsPostResponse_WhenSuccessful() {
        PostResponse expectedPostResponse = PostResponseCreator.createPublishedPostResponse();

        ResponseEntity<PostResponse> responseEntity = postController.findById(expectedPostResponse.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isEqualTo(expectedPostResponse);
    }

    @Test
    @DisplayName("findById throws ResourceNotFoundException when post is not found")
    void findById_ThrowsResourceNotFoundException_WhenPostIsNotFound() {
        long postId = 1;

        BDDMockito.when(postService.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new ResourceNotFoundException(Post.class, postId));

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> postController.findById(postId))
                .withMessageContaining("Could not find resource of type Post with id: " + postId);
    }
}
