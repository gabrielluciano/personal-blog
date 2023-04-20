package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.repositories.PostRepository;
import com.gabrielluciano.blog.util.PostCreator;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        Page<Post> postPage = new PageImpl<>(List.of(PostCreator.createPublishedPost()));

        BDDMockito.when(postRepository.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(postPage);
    }

    @Test
    @DisplayName("list returns page of post responses when successful")
    void list_ReturnsPageOfPostResponses_WhenSuccessful() {
        PostResponse expectedFirstPostResponse = PostResponseCreator.createPublishedPostResponse();

        Page<PostResponse> page = postService.list(PageRequest.of(0, 10), null, null, false);

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(page.getContent().get(0)).isEqualTo(expectedFirstPostResponse);
    }

    @Test
    @DisplayName("list returns empty page of post responses when no post is found")
    void list_ReturnsEmptyPageOfPostResponses_WhenNoPostIsFound() {
        BDDMockito.when(postRepository.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<PostResponse> page = postService.list(PageRequest.of(0, 10), null, null, false);

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("list returns page of post responses containing a specific title when successful")
    void list_ReturnsPageOfPostResponsesContainingASpecificTitle_WhenSuccessful() {
        String title = "Super blog post";
        PostResponse expectedPostResponse = PostResponseCreator.createPublishedPostResponseWithTitleAndSlug(title, title);

        Page<Post> postPage = new PageImpl<>(List.of(PostCreator.createPublishedPostWithTitleAndSlug(title, title)));
        BDDMockito.when(postRepository.findByTitleContainingIgnoreCase(ArgumentMatchers.eq(title),
                        ArgumentMatchers.any(Pageable.class)))
                .thenReturn(postPage);

        Pageable pageable = PageRequest.of(0, 10);
        Page<PostResponse> page = postService.list(pageable, title, null, false);

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(page.getContent().get(0)).isEqualTo(expectedPostResponse);
    }
}
