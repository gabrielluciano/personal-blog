package com.gabrielluciano.blog.integration;

import com.gabrielluciano.blog.dto.tag.TagResponse;
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
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TagControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("list returns page of tag responses when successful")
    void list_ReturnsPageOfTagResponses_WhenSuccessful() {
        Tag savedTag = tagRepository.save(TagCreator.createTagToBeSaved());

        ResponseEntity<RestPageImpl<TagResponse>> responseEntity = restTemplate.exchange("/tags", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {});

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(responseEntity.getBody().getContent().get(0)).isNotNull();

        assertThat(responseEntity.getBody().getContent().get(0).getId())
                .isEqualTo(savedTag.getId());

        assertThat(responseEntity.getBody().getContent().get(0).getName())
                .isEqualTo(savedTag.getName());
    }

    @Test
    @DisplayName("list returns empty page of tag responses when no tag is found")
    void list_ReturnsEmptyPageOfTagResponses_WhenNoTagIsFound() {
        ResponseEntity<RestPageImpl<TagResponse>> responseEntity = restTemplate.exchange("/tags", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {});

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findById returns tag response when successful")
    void findById_ReturnsTagResponse_WhenSuccessful() {
        Tag savedTag = tagRepository.save(TagCreator.createTagToBeSaved());

        ResponseEntity<TagResponse> responseEntity = restTemplate
                .getForEntity("/tags/{id}", TagResponse.class, savedTag.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getId())
                .isEqualTo(savedTag.getId());

        assertThat(responseEntity.getBody().getName())
                .isEqualTo(savedTag.getName());
    }
}
