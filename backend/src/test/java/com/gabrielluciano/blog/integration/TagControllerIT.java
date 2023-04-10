package com.gabrielluciano.blog.integration;

import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.repositories.TagRepository;
import com.gabrielluciano.blog.util.TagCreator;
import com.gabrielluciano.blog.wrappers.RestPageImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class TagControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("list returns page of tags when successful")
    void list_ReturnsPageOfTags_WhenSuccessful() {
        Tag expectedTag = tagRepository.save(TagCreator.createTagToBeSaved());

        ResponseEntity<RestPageImpl<Tag>> responseEntity = restTemplate.exchange("/tags", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {});

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(responseEntity.getBody().getContent().get(0).getName())
                .isEqualTo(expectedTag.getName());

        assertThat(responseEntity.getBody().getContent().get(0).getId())
                .isEqualTo(expectedTag.getId());
    }

    @Test
    @DisplayName("list returns empty page of tags when no tag is found")
    void list_ReturnsEmptyPageOfTags_WhenNoTagIsFound() {
        ResponseEntity<RestPageImpl<Tag>> responseEntity = restTemplate.exchange("/tags", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {});

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
        Tag expectedTag = tagRepository.save(TagCreator.createTagToBeSaved());

        ResponseEntity<Tag> responseEntity = restTemplate.getForEntity("/tags/{id}", Tag.class, expectedTag.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getName()).isEqualTo(expectedTag.getName());

        assertThat(responseEntity.getBody().getId()).isEqualTo(expectedTag.getId());
    }
}
