package com.gabrielluciano.blog.integration;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.error.ErrorDetails;
import com.gabrielluciano.blog.error.ValidationErrorDetails;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.repositories.TagRepository;
import com.gabrielluciano.blog.util.TagCreateRequestCreator;
import com.gabrielluciano.blog.util.TagCreator;
import com.gabrielluciano.blog.wrappers.RestPageImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Log4j2
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

    @Test
    @DisplayName("findById returns error details and status 404 Not Found when tag is not found")
    void findById_ReturnsErrorDetailsAndStatus404NotFound_WhenTagIsNotFound() {
        long tagId = 1;

        ResponseEntity<ErrorDetails> responseEntity = restTemplate
                .getForEntity("/tags/{id}", ErrorDetails.class, tagId);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/tags/" + tagId);

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Tag with id: " + tagId);
    }

    @Test
    @DisplayName("findById returns error details and status 400 Bad Request when id is not valid")
    void findById_ReturnsErrorDetailsAndStatus400BadRequest_WhenIdIsNotValid() {
        ResponseEntity<ErrorDetails> responseEntity = restTemplate
                .getForEntity("/tags/invalidID", ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/tags/invalidID");
    }

    @Test
    @DisplayName("save returns created tag response and status 201 Created when successful")
    void save_ReturnsCreatedTagResponseAndStatus201Created_WhenSuccessful() {
        TagCreateRequest tagCreateRequest = TagCreateRequestCreator.createValidTagCreateRequest();

        ResponseEntity<TagResponse> responseEntity = restTemplate
                .postForEntity("/tags", tagCreateRequest, TagResponse.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getId()).isNotNull();

        assertThat(responseEntity.getBody().getName()).isEqualTo(tagCreateRequest.getName());
    }

    @Test
    @DisplayName("save returns validation error details and status 400 Bad Request when request body is invalid")
    void save_ReturnsValidationErrorDetailsAndStatus400BadRequest_WhenRequestBodyIsInvalid() {
        TagCreateRequest tagCreateRequest = TagCreateRequestCreator.createInvalidTagCreateRequest();

        ResponseEntity<ValidationErrorDetails> responseEntity = restTemplate
                .postForEntity("/tags", tagCreateRequest, ValidationErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ValidationErrorDetails.class);

        assertThat(responseEntity.getBody().getFields()).contains("name", "slug");
        log.info(String.format("Fields messages: %s", responseEntity.getBody().getFieldsMessages()));
    }

    @Test
    @DisplayName("save returns error details with JSON parse error and status 400 Bad Request when request body is an invalid JSON")
    void save_ReturnsErrorDetailsWithJSONParseErrorAndStatus400BadRequest_WhenRequestBodyIsAnInvalidJSON() {
        String invalidJSON = "{ \"name\": \"news\"' }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(invalidJSON, headers);

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/tags", HttpMethod.POST,
                httpEntity, new ParameterizedTypeReference<>() {});

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getTitle()).contains("JSON parse error");
    }

    @Test
    @DisplayName("save returns error details with constraint violation exception and status 400 Bad Request when request name or slug already exists in the database")
    void save_ReturnsErrorDetailsWithConstraintViolationExceptionAndStatus400BadRequest_WhenNameOrSlugAlreadyExistsInTheDatabase() {
        tagRepository.save(TagCreator.createTagToBeSaved());
        TagCreateRequest tagCreateRequest = TagCreateRequestCreator.createValidTagCreateRequest();

        ResponseEntity<ErrorDetails> responseEntity = restTemplate
                .postForEntity("/tags", tagCreateRequest, ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getTitle()).contains("Constraint Violation Exception");
    }
}
