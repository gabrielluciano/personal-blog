package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.services.TagService;
import com.gabrielluciano.blog.util.TagCreateRequestCreator;
import com.gabrielluciano.blog.util.TagCreator;
import com.gabrielluciano.blog.util.TagUpdateRequestCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
class TagControllerTest {

    @InjectMocks
    private TagController tagController;

    @Mock
    private TagService tagService;

    @BeforeEach
    void setUp() {
        Page<Tag> tagPage = new PageImpl<>(List.of(TagCreator.createValidTag()));

        BDDMockito.when(tagService.list(ArgumentMatchers.any()))
                .thenReturn(tagPage);

        BDDMockito.when(tagService.findById(ArgumentMatchers.anyLong()))
                .thenReturn(TagCreator.createValidTag());

        BDDMockito.when(tagService.save(ArgumentMatchers.any(TagCreateRequest.class)))
                .thenReturn(TagCreator.createValidTag());
    }

    @Test
    void list_ReturnsPageOfTags_WhenSuccessful() {
        Tag expectedFirstTag = TagCreator.createValidTag();

        ResponseEntity<Page<Tag>> responseEntity = tagController.list(PageRequest.of(0, 10));

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(responseEntity.getBody().getContent().get(0)).isEqualTo(expectedFirstTag);
    }

    @Test
    void findById_ReturnsTag_WhenSuccessful() {
        Tag expectedTag = TagCreator.createValidTag();

        ResponseEntity<Tag> responseEntity = tagController.findById(1L);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isEqualTo(expectedTag);

        assertThat(responseEntity.getBody().getName()).isEqualTo(expectedTag.getName());
    }

    @Test
    void findById_ReturnsThrowsResourceNotFoundException_WhenTagIsNotFound() {
        long tagId = 1;

        BDDMockito.when(tagService.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new ResourceNotFoundException(Tag.class, tagId));

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tagController.findById(tagId))
                .withMessageContaining("Could not find resource of type Tag with id: " + tagId);
    }

    @Test
    void save_ReturnsCreatedTagAndStatus201Created_WhenSuccessful() {
        TagCreateRequest tagCreateRequest = TagCreateRequestCreator.createValidTagCreateRequest();

        ResponseEntity<Tag> responseEntity = tagController.save(tagCreateRequest);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getId()).isNotNull();

        assertThat(responseEntity.getBody().getName()).isEqualTo(tagCreateRequest.getName());
    }

    @Test
    void update_ReturnsStatus204NoContent_WhenSuccessful() {
        TagUpdateRequest tagUpdateRequest = TagUpdateRequestCreator.createValidTagUpdateRequest();

        ResponseEntity<Void> responseEntity = tagController.update(tagUpdateRequest, 1);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    void update_ThrowsResourceNotFoundException_WhenTagIsNotFound() {
        long tagId = 1;

        TagUpdateRequest tagUpdateRequest = TagUpdateRequestCreator.createValidTagUpdateRequest();

        BDDMockito.doThrow(new ResourceNotFoundException(Tag.class, tagId))
                .when(tagService).update(ArgumentMatchers.any());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tagController.update(tagUpdateRequest, tagId))
                .withMessageContaining("Could not find resource of type Tag with id: " + tagId);
    }
}
