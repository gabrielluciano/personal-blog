package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.services.PostService;
import com.gabrielluciano.blog.util.PostResponseCreator;
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
}
