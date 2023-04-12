package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("findFirstByNameIgnoreCaseOrSlugIgnoreCase returns optional of tag when tag with same name is found")
    void findFirstByNameIgnoreCaseOrSlugIgnoreCase_ReturnsOptionalOfTag_WhenTagWithSameNameIsFound() {
        Tag savedTag = tagRepository.save(Tag.builder()
                .name("Games")
                .slug("games")
                .build());

        Optional<Tag> tagOptional = tagRepository.findFirstByNameIgnoreCaseOrSlugIgnoreCase("games", "wrong slug");

        assertThat(tagOptional)
                .isNotNull()
                .isPresent()
                .contains(savedTag);
    }

    @Test
    @DisplayName("findFirstByNameIgnoreCaseOrSlugIgnoreCase returns optional of tag when tag with same slug is found")
    void findFirstByNameIgnoreCaseOrSlugIgnoreCase_ReturnsOptionalOfTag_WhenTagWithSameSlugIsFound() {
        Tag savedTag = tagRepository.save(Tag.builder()
                .name("Games")
                .slug("games")
                .build());

        Optional<Tag> tagOptional = tagRepository.findFirstByNameIgnoreCaseOrSlugIgnoreCase("wrong name", "Games");

        assertThat(tagOptional)
                .isNotNull()
                .isPresent()
                .contains(savedTag);
    }

    @Test
    @DisplayName("findFirstByNameIgnoreCaseOrSlugIgnoreCase returns optional empty when no tag is found")
    void findFirstByNameIgnoreCaseOrSlugIgnoreCase_ReturnsOptionalEmpty_WhenNoTagIsFound() {
        tagRepository.save(Tag.builder()
                .name("games")
                .slug("games")
                .build());

        Optional<Tag> tagOptional = tagRepository.findFirstByNameIgnoreCaseOrSlugIgnoreCase("wrong name", "wrong slug");

        assertThat(tagOptional)
                .isNotNull()
                .isEmpty();
    }
}
