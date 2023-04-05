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

        BDDMockito.doNothing().when(tagService)
                .update(ArgumentMatchers.any(TagUpdateRequest.class));

        BDDMockito.doNothing().when(tagService)
                .deleteById(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("list returns page of tags when successful")
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
    @DisplayName("list returns empty page of tags when no tag is found")
    void list_ReturnsEmptyPageOfTags_WhenNoTagIsFound() {
        BDDMockito.when(tagService.list(ArgumentMatchers.any()))
                .thenReturn(Page.empty());

        ResponseEntity<Page<Tag>> responseEntity = tagController.list(PageRequest.of(0, 10));

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findById returns tag when successful")
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
    @DisplayName("findById throws ResourceNotFoundException when tag is not found")
    void findById_ThrowsResourceNotFoundException_WhenTagIsNotFound() {
        long tagId = 1;

        BDDMockito.when(tagService.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new ResourceNotFoundException(Tag.class, tagId));

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tagController.findById(tagId))
                .withMessageContaining("Could not find resource of type Tag with id: " + tagId);
    }

    @Test
    @DisplayName("save returns created tag and status 201 Created when successful")
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
    @DisplayName("update returns status 204 No Content when successful")
    void update_ReturnsStatus204NoContent_WhenSuccessful() {
        TagUpdateRequest tagUpdateRequest = TagUpdateRequestCreator.createValidTagUpdateRequest();

        ResponseEntity<Void> responseEntity = tagController.update(tagUpdateRequest, 1L);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update throws ResourceNotFoundException when tag is not found")
    void update_ThrowsResourceNotFoundException_WhenTagIsNotFound() {
        long tagId = 1;

        TagUpdateRequest tagUpdateRequest = TagUpdateRequestCreator.createValidTagUpdateRequest();

        BDDMockito.doThrow(new ResourceNotFoundException(Tag.class, tagId))
                .when(tagService).update(ArgumentMatchers.any());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tagController.update(tagUpdateRequest, tagId))
                .withMessageContaining("Could not find resource of type Tag with id: " + tagId);
    }

    @Test
    @DisplayName("deleteById returns status 204 No Content when successful")
    void deleteById_ReturnsStatus204NoContent_WhenSuccessful() {
        ResponseEntity<Void> responseEntity = tagController.deleteById(1L);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("deleteById throws ResourceNotFoundException when tag is not found")
    void deleteById_ThrowsResourceNotFoundException_WhenTagIsNotFound() {
        long tagId = 1;

        BDDMockito.doThrow(new ResourceNotFoundException(Tag.class, tagId))
                .when(tagService).deleteById(ArgumentMatchers.anyLong());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tagController.deleteById(tagId))
                .withMessageContaining("Could not find resource of type Tag with id: " + tagId);
    }
}
