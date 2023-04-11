package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.repositories.TagRepository;
import com.gabrielluciano.blog.util.TagCreateRequestCreator;
import com.gabrielluciano.blog.util.TagCreator;
import com.gabrielluciano.blog.util.TagResponseCreator;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        Page<Tag> tagPage = new PageImpl<>(List.of(TagCreator.createValidTag()));

        BDDMockito.when(tagRepository.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(tagPage);

        BDDMockito.when(tagRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(TagCreator.createValidTag()));

        BDDMockito.when(tagRepository.save(ArgumentMatchers.any(Tag.class)))
                .thenReturn(TagCreator.createValidTag());

        BDDMockito.doNothing().when(tagRepository)
                .deleteById(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("list returns page of tag responses when successful")
    void list_ReturnsPageOfTagResponses_WhenSuccessful() {
        TagResponse expectedFirstTagResponse = TagResponseCreator.createValidTagResponse();

        Page<TagResponse> page = tagService.list(PageRequest.of(0, 10));

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(page.getContent().get(0)).isEqualTo(expectedFirstTagResponse);
    }

    @Test
    @DisplayName("list returns empty page of tag responses when no tag is found")
    void list_ReturnsEmptyPageOfTagResponses_WhenNoTagIsFound() {
        BDDMockito.when(tagRepository.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<TagResponse> page = tagService.list(PageRequest.of(0, 10));

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findByIdOrThrowResourceNotFoundException returns tag response when successful")
    void findByIdOrThrowResourceNotFoundException_ReturnsTagResponse_WhenSuccessful() {
        TagResponse expectedTagResponse = TagResponseCreator.createValidTagResponse();

        TagResponse tagResponse = tagService.findByIdOrThrowResourceNotFoundException(expectedTagResponse.getId());

        assertThat(tagResponse)
                .isNotNull()
                .isEqualTo(expectedTagResponse);
    }

    @Test
    @DisplayName("findByIdOrThrowResourceNotFoundException throws ResourceNotFoundException when tag is not found")
    void findByIdOrThrowResourceNotFoundException_ThrowsResourceNotFoundException_WhenTagIsNotFound() {
        long tagId = 1;

        BDDMockito.when(tagRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tagService.findByIdOrThrowResourceNotFoundException(tagId))
                .withMessageContaining("Could not find resource of type Tag with id: " + tagId);
    }

    @Test
    @DisplayName("save returns created tag response when successful")
    void save_ReturnsCreatedTagResponse_WhenSuccessful() {
        TagCreateRequest tagCreateRequest = TagCreateRequestCreator.createValidTagCreateRequest();

        TagResponse createdTagResponse = tagService.save(tagCreateRequest);

        assertThat(createdTagResponse).isNotNull();

        assertThat(createdTagResponse.getId()).isNotNull();

        assertThat(createdTagResponse.getName()).isEqualTo(tagCreateRequest.getName());
    }

    @Test
    @DisplayName("update updates tag when successful")
    void update_UpdatesTag_WhenSuccessful() {
        TagUpdateRequest tagUpdateRequest = TagUpdateRequestCreator.createValidTagUpdateRequest();

        assertThatNoException().isThrownBy(() -> tagService.update(tagUpdateRequest, 1L));
    }

    @Test
    @DisplayName("update throws ResourceNotFoundException when tag is not found")
    void update_ThrowsResourceNotFoundException_WhenTagIsNotFound() {
        long tagId = 1;

        TagUpdateRequest tagUpdateRequest = TagUpdateRequestCreator.createValidTagUpdateRequest();

        BDDMockito.when(tagRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tagService.update(tagUpdateRequest, tagId))
                .withMessageContaining("Could not find resource of type Tag with id: " + tagId);
    }

    @Test
    @DisplayName("deleteById deletes tag when successful")
    void deleteById_DeletesTag_WhenSuccessful() {
        assertThatNoException().isThrownBy(() -> tagService.deleteById(1L));
    }

    @Test
    @DisplayName("deleteById throws ResourceNotFoundException when tag is not found")
    void deleteById_ThrowsResourceNotFoundException_WhenTagIsNotFound() {
        long tagId = 1;

        BDDMockito.when(tagRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tagService.deleteById(tagId))
                .withMessageContaining("Could not find resource of type Tag with id: " + tagId);
    }
}
