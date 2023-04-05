package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.repositories.TagRepository;
import com.gabrielluciano.blog.util.TagCreator;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
    }

    @Test
    void list_ReturnsPageOfTags_WhenSuccessful() {
        Tag expectedFirstTag = TagCreator.createValidTag();

        Page<Tag> page = tagService.list(PageRequest.of(0, 10));

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(page.getContent().get(0)).isEqualTo(expectedFirstTag);
    }

    @Test
    void list_ReturnsEmptyPageOfTags_WhenNoTagIsFound() {
        BDDMockito.when(tagService.list(ArgumentMatchers.any()))
                .thenReturn(Page.empty());

        Page<Tag> page = tagService.list(PageRequest.of(0, 10));

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isEmpty();
    }

    @Test
    void findById_ReturnsTag_WhenSuccessful() {
        Tag expectedTag = TagCreator.createValidTag();

        Tag tag = tagService.findById(expectedTag.getId());

        assertThat(tag)
                .isNotNull()
                .isEqualTo(expectedTag);
    }

    @Test
    void findById_ThrowsResourceNotFoundException_WhenNotTagIsFound() {
        long tagId = 1;

        BDDMockito.when(tagRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> tagService.findById(tagId))
                .withMessageContaining("Could not find resource of type Tag with id: " + tagId);
    }
}
