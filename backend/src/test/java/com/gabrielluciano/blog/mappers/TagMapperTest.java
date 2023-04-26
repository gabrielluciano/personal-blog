package com.gabrielluciano.blog.mappers;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.util.TagCreateRequestCreator;
import com.gabrielluciano.blog.util.TagCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TagMapperTest {

    @Test
    @DisplayName("tagCreateRequestToTag returns tag when successful")
    void tagCreateRequestToTag_ReturnsTag_WhenSuccessful() {
        TagCreateRequest tagCreateRequest = TagCreateRequestCreator.createValidTagCreateRequest();

        Tag tag = TagMapper.INSTANCE.tagCreateRequestToTag(tagCreateRequest);

        assertThat(tag.getName()).isNotNull().isEqualTo(tagCreateRequest.getName());

        assertThat(tag.getSlug()).isNotNull().isEqualTo(tagCreateRequest.getSlug());

        assertThat(tag.getDescription()).isNotNull().isEqualTo(tagCreateRequest.getDescription());

        assertThat(tag.getId()).isNull();
    }

    @Test
    @DisplayName("tagToTagResponse returns tag response when successful")
    void tagToTagResponse_ReturnsTagResponse_WhenSuccessful() {
        Tag tag = TagCreator.createValidTag();

        TagResponse tagResponse = TagMapper.INSTANCE.tagToTagResponse(tag);

        assertThat(tagResponse.getName()).isNotNull().isEqualTo(tag.getName());

        assertThat(tagResponse.getSlug()).isNotNull().isEqualTo(tag.getSlug());

        assertThat(tagResponse.getDescription()).isNotNull().isEqualTo(tag.getDescription());

        assertThat(tagResponse.getId()).isNotNull().isEqualTo(tag.getId());
    }

    @Test
    @DisplayName("updateTagFromTagUpdateRequest updates tag attributes when successful")
    void updateTagFromTagUpdateRequestUpdatesTagAttributes_WhenSuccessful() {
        String expectedName = "updated name";
        String expectedSlug = "updated slug";
        String expectedDescription = "updated description";

        Tag tag = TagCreator.createValidTag();

        TagUpdateRequest tagUpdateRequest = TagUpdateRequest.builder()
                .name(expectedName)
                .slug(expectedSlug)
                .description(expectedDescription)
                .build();

        TagMapper.INSTANCE.updateTagFromTagUpdateRequest(tagUpdateRequest, tag);

        assertThat(tag.getName()).isEqualTo(expectedName);

        assertThat(tag.getSlug()).isEqualTo(expectedSlug);

        assertThat(tag.getDescription()).isEqualTo(expectedDescription);

        assertThat(tag.getId()).isNotNull();

        assertThat(tag.getUuid()).isNotNull();
    }
}
