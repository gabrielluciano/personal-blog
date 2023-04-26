package com.gabrielluciano.blog.mappers;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.util.PostCreateRequestCreator;
import com.gabrielluciano.blog.util.PostCreator;
import com.gabrielluciano.blog.util.TagCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PostMapperTest {

    @Test
    @DisplayName("postToPostResponse returns published PostResponse when successful")
    void postToPostResponse_ReturnsPublishedPostResponse_WhenSuccessful() {
        Tag tag = TagCreator.createValidTag();
        Post post = PostCreator.createPublishedPostWithTags(Set.of(tag));

        PostResponse postResponse = PostMapper.INSTANCE.postToPostResponse(post);

        assertThat(postResponse.getId()).isNotNull().isEqualTo(post.getId());

        assertThat(postResponse.getTitle()).isNotNull().isEqualTo(post.getTitle());

        assertThat(postResponse.getSubtitle()).isNotNull().isEqualTo(post.getSubtitle());

        assertThat(postResponse.getContent()).isNotNull().isEqualTo(post.getContent());

        assertThat(postResponse.getMetaTitle()).isNotNull().isEqualTo(post.getMetaTitle());

        assertThat(postResponse.getMetaDescription()).isNotNull().isEqualTo(post.getMetaDescription());

        assertThat(postResponse.getSlug()).isNotNull().isEqualTo(post.getSlug());

        assertThat(postResponse.getImageUrl()).isNotNull().isEqualTo(post.getImageUrl());

        assertThat(postResponse.getPublished()).isNotNull().isEqualTo(post.getPublished());

        assertThat(postResponse.getCreatedAt()).isNotNull().isEqualTo(post.getCreatedAt());

        assertThat(postResponse.getUpdatedAt()).isNotNull().isEqualTo(post.getUpdatedAt());

        assertThat(postResponse.getPublishedAt()).isNotNull().isEqualTo(post.getPublishedAt());

        assertThat(postResponse.getTags()).contains(TagMapper.INSTANCE.tagToTagResponse(tag));
    }

    @Test
    @DisplayName("postToPostResponse returns unpublished PostResponse when successful")
    void postToPostResponse_ReturnsUnpublishedPostResponse_WhenSuccessful() {
        Tag tag = TagCreator.createValidTag();
        Post post = PostCreator.createUnpublishedPostWithTags(Set.of(tag));

        PostResponse postResponse = PostMapper.INSTANCE.postToPostResponse(post);

        assertThat(postResponse.getId()).isNotNull().isEqualTo(post.getId());

        assertThat(postResponse.getTitle()).isNotNull().isEqualTo(post.getTitle());

        assertThat(postResponse.getSubtitle()).isNotNull().isEqualTo(post.getSubtitle());

        assertThat(postResponse.getContent()).isNotNull().isEqualTo(post.getContent());

        assertThat(postResponse.getMetaTitle()).isNotNull().isEqualTo(post.getMetaTitle());

        assertThat(postResponse.getMetaDescription()).isNotNull().isEqualTo(post.getMetaDescription());

        assertThat(postResponse.getSlug()).isNotNull().isEqualTo(post.getSlug());

        assertThat(postResponse.getImageUrl()).isNotNull().isEqualTo(post.getImageUrl());

        assertThat(postResponse.getPublished()).isNotNull().isEqualTo(post.getPublished());

        assertThat(postResponse.getCreatedAt()).isNotNull().isEqualTo(post.getCreatedAt());

        assertThat(postResponse.getUpdatedAt()).isNotNull().isEqualTo(post.getUpdatedAt());

        assertThat(postResponse.getPublishedAt()).isNull();

        assertThat(postResponse.getTags()).contains(TagMapper.INSTANCE.tagToTagResponse(tag));
    }

    @Test
    @DisplayName("postCreateRequestToPost returns unpublished Post when successful")
    void postCreateRequestToPost_ReturnsUnpublishedPost_WhenSuccessful() {
        PostCreateRequest postCreateRequest = PostCreateRequestCreator.createValidPostCreateRequest();

        Post post = PostMapper.INSTANCE.postCreateRequestToPost(postCreateRequest);

        assertThat(post.getId()).isNull();

        assertThat(post.getUuid()).isNotNull();

        assertThat(post.getTitle()).isNotNull().isEqualTo(postCreateRequest.getTitle());

        assertThat(post.getSubtitle()).isNotNull().isEqualTo(postCreateRequest.getSubtitle());

        assertThat(post.getContent()).isNotNull().isEqualTo(postCreateRequest.getContent());

        assertThat(post.getMetaTitle()).isNotNull().isEqualTo(postCreateRequest.getMetaTitle());

        assertThat(post.getMetaDescription()).isNotNull().isEqualTo(postCreateRequest.getMetaDescription());

        assertThat(post.getSlug()).isNotNull().isEqualTo(postCreateRequest.getSlug());

        assertThat(post.getImageUrl()).isNotNull().isEqualTo(postCreateRequest.getImageUrl());

        assertThat(post.getPublished()).isNotNull().isFalse();

        assertThat(post.getPublishedAt()).isNull();

        assertThat(post.getCreatedAt()).isNotNull().isInstanceOf(LocalDateTime.class);

        assertThat(post.getUpdatedAt()).isNotNull().isInstanceOf(LocalDateTime.class);

        assertThat(post.getTags()).isNull();
    }

    @Test
    @DisplayName("updatePostFromPostUpdateRequest updates Post when successful")
    void updatePostFromPostUpdateRequest_UpdatesPost_WhenSuccessful() {
        String expectedTitle = "expected title";
        String expectedSubtitle = "expected subtitle";
        String expectedContent = "expected content";
        String expectedMetaTitle = "expected meta title";
        String expectedMetaDescription = "expected meta description";
        String expectedSlug = "expected slug";
        String expectedImageUrl = "expected image url";

        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
                .title(expectedTitle)
                .subtitle(expectedSubtitle)
                .content(expectedContent)
                .metaTitle(expectedMetaTitle)
                .metaDescription(expectedMetaDescription)
                .slug(expectedSlug)
                .imageUrl(expectedImageUrl)
                .build();

        Post post = PostCreator.createPublishedPost();

        LocalDateTime updatedAtBefore = post.getUpdatedAt();
        Set<Tag> tagsBefore = post.getTags();

        PostMapper.INSTANCE.updatePostFromPostUpdateRequest(postUpdateRequest, post);

        assertThat(post.getId()).isNotNull();

        assertThat(post.getUuid()).isNotNull();

        assertThat(post.getTitle()).isNotNull().isEqualTo(expectedTitle);

        assertThat(post.getSubtitle()).isNotNull().isEqualTo(expectedSubtitle);

        assertThat(post.getContent()).isNotNull().isEqualTo(expectedContent);

        assertThat(post.getMetaTitle()).isNotNull().isEqualTo(expectedMetaTitle);

        assertThat(post.getMetaDescription()).isNotNull().isEqualTo(expectedMetaDescription);

        assertThat(post.getSlug()).isNotNull().isEqualTo(expectedSlug);

        assertThat(post.getImageUrl()).isNotNull().isEqualTo(expectedImageUrl);

        assertThat(post.getPublished()).isNotNull().isTrue();

        assertThat(post.getPublishedAt()).isNotNull().isInstanceOf(LocalDateTime.class);

        assertThat(post.getCreatedAt()).isNotNull().isInstanceOf(LocalDateTime.class);

        assertThat(post.getUpdatedAt())
                .isNotNull()
                .isInstanceOf(LocalDateTime.class)
                .isAfter(updatedAtBefore);

        assertThat(post.getTags()).isEqualTo(tagsBefore);
    }
}
