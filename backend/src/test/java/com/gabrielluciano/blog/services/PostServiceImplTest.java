package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.repositories.PostRepository;
import com.gabrielluciano.blog.util.PostCreator;
import com.gabrielluciano.blog.util.PostResponseCreator;
import com.gabrielluciano.blog.util.TagCreator;
import com.gabrielluciano.blog.util.TagResponseCreator;
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
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        Page<Post> publishedPostPage = new PageImpl<>(List.of(PostCreator.createPublishedPost()));
        Page<Post> unpublishedPostPage = new PageImpl<>(List.of(PostCreator.createUnpublishedPost()));
        Page<Post> publishedPostWithTagPage = new PageImpl<>(List.of(PostCreator
                .createPublishedPostWithTags(Set.of(TagCreator.createValidTag()))));

        BDDMockito.when(postRepository.findAllByPublishedIsTrue(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(publishedPostPage);

        BDDMockito.when(postRepository.findAllByPublishedIsFalse(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(unpublishedPostPage);

        BDDMockito.when(postRepository.findAllByPublishedIsTrueAndTagsId(ArgumentMatchers.anyLong(),
                        ArgumentMatchers.any(Pageable.class)))
                .thenReturn(publishedPostWithTagPage);

        BDDMockito.when(postRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(PostCreator.createPublishedPost()));

        BDDMockito.when(postRepository.findByPublishedIsTrueAndSlug(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(PostCreator.createPublishedPost()));
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
        BDDMockito.when(postRepository.findAllByPublishedIsTrue(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<PostResponse> page = postService.list(PageRequest.of(0, 10), null, null, false);

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("list returns page of post responses containing a specific title when title parameter is specified")
    void list_ReturnsPageOfPostResponsesContainingASpecificTitle_WhenTitleParameterIsSpecified() {
        String title = "Super blog post";
        PostResponse expectedPostResponse = PostResponseCreator.createPublishedPostResponseWithTitleAndSlug(title, title);

        Page<Post> postPage = new PageImpl<>(List.of(PostCreator.createPublishedPostWithTitleAndSlug(title, title)));
        BDDMockito.when(postRepository.findAllByPublishedIsTrueAndTitleContainingIgnoreCase(ArgumentMatchers.eq(title),
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

    @Test
    @DisplayName("list returns page of unpublished post responses when drafts parameter is true")
    void list_ReturnsPageOfUnpublishedPostResponses_WhenDraftsParameterIsTrue() {
        PostResponse expectedPostResponse = PostResponseCreator.createUnpublishedPostResponse();

        Pageable pageable = PageRequest.of(0, 10);
        Page<PostResponse> page = postService.list(pageable, null, null, true);

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(page.getContent().get(0)).isEqualTo(expectedPostResponse);
    }

    @Test
    @DisplayName("list returns page of post responses containing a specific tag response when tag parameter is specified")
    void list_ReturnsPageOfPostResponsesContainingASpecificTagResponse_WhenTagParameterIsSpecified() {
        PostResponse expectedPostResponse = PostResponseCreator
                .createPublishedPostResponseWithTags(Set.of(TagCreator.createValidTag()));
        TagResponse expectedTagResponse = TagResponseCreator.createValidTagResponse();

        Pageable pageable = PageRequest.of(0, 10);
        Page<PostResponse> page = postService.list(pageable, null, expectedTagResponse.getId(), false);

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(page.getContent().get(0)).isEqualTo(expectedPostResponse);
        assertThat(page.getContent().get(0).getTags()).contains(expectedTagResponse);
    }

    @Test
    @DisplayName("findById returns post response when successful")
    void findById_ReturnsPostResponse_WhenSuccessful() {
        PostResponse expectedPostResponse = PostResponseCreator.createPublishedPostResponse();

        PostResponse postResponse = postService.findById(expectedPostResponse.getId());

        assertThat(postResponse)
                .isNotNull()
                .isEqualTo(expectedPostResponse);
    }

    @Test
    @DisplayName("findById throws ResourceNotFoundException when post is not found")
    void findById_ThrowsResourceNotFoundException_WhenPostIsNotFound() {
        long postId = 1;

        BDDMockito.when(postRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> postService.findById(postId))
                .withMessageContaining("Could not find resource of type Post with identifier: " + postId);
    }

    @Test
    @DisplayName("findBySlug returns post response when successful")
    void findBySlug_ReturnsPostResponse_WhenSuccessful() {
        PostResponse expectedPostResponse = PostResponseCreator.createPublishedPostResponse();

        PostResponse postResponse = postService.findBySlug(expectedPostResponse.getSlug());

        assertThat(postResponse)
                .isNotNull()
                .isEqualTo(expectedPostResponse);
    }

    @Test
    @DisplayName("findBySlug throws ResourceNotFoundException when post is not found")
    void findBySlug_ThrowsResourceNotFoundException_WhenPostIsNotFound() {
        String slug = "some-slug";

        BDDMockito.when(postRepository.findByPublishedIsTrueAndSlug(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> postService.findBySlug(slug))
                .withMessageContaining("Could not find resource of type Post with identifier: " + slug);
    }
}
