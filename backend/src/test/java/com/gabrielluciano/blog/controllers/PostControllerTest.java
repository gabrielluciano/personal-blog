package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.services.PostService;
import com.gabrielluciano.blog.util.PostCreateRequestCreator;
import com.gabrielluciano.blog.util.PostResponseCreator;
import com.gabrielluciano.blog.util.PostUpdateRequestCreator;
import com.gabrielluciano.blog.util.TagCreator;
import com.gabrielluciano.blog.util.TagResponseCreator;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @BeforeEach
    void setUp() {
        Page<PostResponse> publishedPostResponsePage = new PageImpl<>(List
                .of(PostResponseCreator.createPublishedPostResponse()));

        Page<PostResponse> unpublishedPostResponsePage = new PageImpl<>(List
                .of(PostResponseCreator.createUnpublishedPostResponse()));

        Page<PostResponse> publishedPostResponsePageWithTag = new PageImpl<>(List.of(PostResponseCreator
                .createPublishedPostResponseWithTags(Set.of(TagCreator.createValidTag()))));

        BDDMockito.when(postService.list(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.isNull(),
                        ArgumentMatchers.isNull(), ArgumentMatchers.eq(false)))
                .thenReturn(publishedPostResponsePage);

        BDDMockito.when(postService.list(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.isNull(),
                        ArgumentMatchers.isNull(), ArgumentMatchers.eq(true)))
                .thenReturn(unpublishedPostResponsePage);

        BDDMockito.when(postService.list(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.isNull(),
                        ArgumentMatchers.anyLong(), ArgumentMatchers.eq(false)))
                .thenReturn(publishedPostResponsePageWithTag);

        BDDMockito.when(postService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(PostResponseCreator.createPublishedPostResponse());

        BDDMockito.when(postService.findBySlug(ArgumentMatchers.anyString()))
                        .thenReturn(PostResponseCreator.createPublishedPostResponse());

        BDDMockito.when(postService.save(ArgumentMatchers.any(PostCreateRequest.class)))
                .thenReturn(PostResponseCreator.createUnpublishedPostResponse());
    }

    @Test
    @DisplayName("list returns page of post responses when successful")
    void list_ReturnsPageOfPostResponses_WhenSuccessful() {
        PostResponse expectedFirstPostResponse = PostResponseCreator.createPublishedPostResponse();

        ResponseEntity<Page<PostResponse>> responseEntity = postController
                .list(Pageable.unpaged(), null, null, false);

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
        BDDMockito.when(postService.list(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.isNull(),
                        ArgumentMatchers.isNull(), ArgumentMatchers.eq(false)))
                .thenReturn(Page.empty());

        ResponseEntity<Page<PostResponse>> responseEntity = postController.list(Pageable.unpaged(),
                null, null, false);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("list returns page of post responses containing a specific title when successful")
    void list_ReturnsPageOfPostResponsesContainingASpecificTitle_WhenSuccessful() {
        String title = "Super blog post";
        String slug = "super-blog-post";

        PostResponse expectedFirstPostResponse = PostResponseCreator.createPublishedPostResponseWithTitleAndSlug(
                title, slug);

        Page<PostResponse> postResponsePage = new PageImpl<>(List.of(PostResponseCreator
                .createPublishedPostResponseWithTitleAndSlug(title, slug)));

        BDDMockito.when(postService.list(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.anyString(),
                        ArgumentMatchers.isNull(), ArgumentMatchers.eq(false)))
                .thenReturn(postResponsePage);

        ResponseEntity<Page<PostResponse>> responseEntity = postController
                .list(Pageable.unpaged(), expectedFirstPostResponse.getTitle().toLowerCase(),
                        null, false);

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
    @DisplayName("list returns page of unpublished post responses when drafts is true")
    void list_ReturnsPageOfUnpublishedPostResponses_WhenDraftsIsTrue() {
        PostResponse expectedFirstPostResponse = PostResponseCreator.createUnpublishedPostResponse();

        ResponseEntity<Page<PostResponse>> responseEntity = postController
                .list(Pageable.unpaged(), null, null, true);

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
    @DisplayName("list returns page of post responses containing a specific tag response when tag parameter is passed")
    void list_ReturnsPageOfPostResponsesContainingASpecificTagResponse_WhenTagParameterIsPassed() {
        TagResponse tagResponse = TagResponseCreator.createValidTagResponse();
        PostResponse expectedFirstPostResponse = PostResponseCreator
                .createPublishedPostResponseWithTags(Set.of(TagCreator.createValidTag()));

        ResponseEntity<Page<PostResponse>> responseEntity = postController
                .list(Pageable.unpaged(), null, tagResponse.getId(), false);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(responseEntity.getBody().getContent().get(0)).isEqualTo(expectedFirstPostResponse);

        assertThat(responseEntity.getBody().getContent().get(0).getTags())
                .isNotNull()
                .isNotEmpty()
                .contains(tagResponse);
    }

    @Test
    @DisplayName("findById returns post response when successful")
    void findById_ReturnsPostResponse_WhenSuccessful() {
        PostResponse expectedPostResponse = PostResponseCreator.createPublishedPostResponse();

        ResponseEntity<PostResponse> responseEntity = postController.findById(expectedPostResponse.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

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
                .withMessageContaining("Could not find resource of type Post with identifier: " + postId);
    }

    @Test
    @DisplayName("findBySlug returns post response when successful")
    void findBySlug_ReturnsPostResponse_WhenSuccessful() {
        PostResponse expectedPostResponse = PostResponseCreator.createPublishedPostResponse();

        ResponseEntity<PostResponse> responseEntity = postController.findBySlug(expectedPostResponse.getSlug());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isEqualTo(expectedPostResponse);

        assertThat(responseEntity.getBody().getSlug()).isEqualTo(expectedPostResponse.getSlug());
    }

    @Test
    @DisplayName("findBySlug throws ResourceNotFoundException when post is not found")
    void findBySlug_ThrowsResourceNotFoundException_WhenPostIsNotFound() {
        String slug = "some-slug";

        BDDMockito.when(postService.findBySlug(ArgumentMatchers.anyString()))
                .thenThrow(new ResourceNotFoundException(Post.class, slug));

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> postController.findBySlug(slug))
                .withMessageContaining("Could not find resource of type Post with identifier: " + slug);
    }

    @Test
    @DisplayName("save returns created post response and status 201 Created when successful")
    void save_ReturnsCreatedPostResponseAndStatus201Created_WhenSuccessful() {
        PostCreateRequest postCreateRequest = PostCreateRequestCreator.createValidPostCreateRequest();

        ResponseEntity<PostResponse> responseEntity = postController.save(postCreateRequest);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getTitle()).isEqualTo(postCreateRequest.getTitle());

        assertThat(responseEntity.getBody().getContent()).isEqualTo(postCreateRequest.getContent());

        assertThat(responseEntity.getBody().getPublished()).isFalse();

        assertThat(responseEntity.getBody().getCreatedAt()).isNotNull();

        assertThat(responseEntity.getBody().getUpdatedAt()).isNotNull();

        assertThat(responseEntity.getBody().getPublishedAt()).isNull();
    }

    @Test
    @DisplayName("update returns status 204 No Content when successful")
    void update_ReturnsStatus204NoContent_WhenSuccessful() {
        PostUpdateRequest postUpdateRequest = PostUpdateRequestCreator.createValidPostUpdateRequest();

        ResponseEntity<Void> responseEntity = postController.update(postUpdateRequest, 1L);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update throws ResourceNotFoundException when post is not found")
    void update_ThrowsResourceNotFoundException_WhenPostIsNotFound() {
        long postId = 1;

        PostUpdateRequest postUpdateRequest = PostUpdateRequestCreator.createValidPostUpdateRequest();

        BDDMockito.doThrow(new ResourceNotFoundException(Post.class, postId))
                .when(postService).update(ArgumentMatchers.any(PostUpdateRequest.class), ArgumentMatchers.anyLong());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> postController.update(postUpdateRequest, postId))
                .withMessageContaining("Could not find resource of type Post with identifier: " + postId);
    }

    @Test
    @DisplayName("deleteById returns status 204 No Content when successful")
    void deleteById_ReturnsStatus204NoContent_WhenSuccessful() {
        long postId = 1;

        ResponseEntity<Void> responseEntity = postController.deleteById(postId);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("deleteById throws ResourceNotFoundException when post is not found")
    void deleteById_ThrowsResourceNotFoundException_WhenPostIsNotFound() {
        long postId = 1;

        BDDMockito.doThrow(new ResourceNotFoundException(Post.class, postId))
                .when(postService).deleteById(ArgumentMatchers.anyLong());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> postController.deleteById(postId))
                .withMessageContaining("Could not find resource of type Post with identifier: " + postId);
    }

    @Test
    @DisplayName("addTag returns status 204 No Content when successful")
    void addTag_ReturnsStatus204NoContent_WhenSuccessful() {
        long postId = 1;
        long tagId = 1;

        ResponseEntity<Void> responseEntity = postController.addTag(postId, tagId);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("addTag throws ResourceNotFoundException when post is not found")
    void addTag_ThrowsResourceNotFoundException_WhenPostIsNotFound() {
        long postId = 1;
        long tagId = 1;

        BDDMockito.doThrow(new ResourceNotFoundException(Post.class, postId))
                .when(postService).addTag(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> postController.addTag(postId, tagId))
                .withMessageContaining("Could not find resource of type Post with identifier: " + postId);
    }

    @Test
    @DisplayName("removeTag returns status 204 No Content when successful")
    void removeTag_ReturnsStatus204NoContent_WhenSuccessful() {
        long postId = 1;
        long tagId = 1;

        ResponseEntity<Void> responseEntity = postController.removeTag(postId, tagId);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("removeTag throws ResourceNotFoundException when post is not found")
    void removeTag_ThrowsResourceNotFoundException_WhenPostIsNotFound() {
        long postId = 1;
        long tagId = 1;

        BDDMockito.doThrow(new ResourceNotFoundException(Post.class, postId))
                .when(postService).removeTag(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> postController.removeTag(postId, tagId))
                .withMessageContaining("Could not find resource of type Post with identifier: " + postId);
    }
}
