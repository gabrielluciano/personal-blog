package com.gabrielluciano.blog.integration;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.error.ErrorDetails;
import com.gabrielluciano.blog.error.ValidationErrorDetails;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.repositories.TagRepository;
import com.gabrielluciano.blog.util.AuthUtil;
import com.gabrielluciano.blog.util.TagCreateRequestCreator;
import com.gabrielluciano.blog.util.TagCreator;
import com.gabrielluciano.blog.util.TagUpdateRequestCreator;
import com.gabrielluciano.blog.wrappers.RestPageImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Log4j2
class TagControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private AuthUtil authUtil;

    private HttpHeaders httpHeadersWithRoleEditorJwt;
    private HttpHeaders httpHeadersWithNoRoleJwt;

    @BeforeEach
    void setUp() {
        httpHeadersWithRoleEditorJwt = authUtil.getHttpHeadersForEditorUser();
        httpHeadersWithNoRoleJwt = authUtil.getHttpHeadersForUser();
    }

    @Test
    @DisplayName("list returns page of tag responses when successful")
    void list_ReturnsPageOfTagResponses_WhenSuccessful() {
        Tag savedTag = tagRepository.save(TagCreator.createNewsTagToBeSaved());

        ResponseEntity<RestPageImpl<TagResponse>> responseEntity = restTemplate.exchange("/tags", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });

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
                null, new ParameterizedTypeReference<>() {
                });

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
        Tag savedTag = tagRepository.save(TagCreator.createNewsTagToBeSaved());

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
                .isEqualTo("Could not find resource of type Tag with identifier: " + tagId);
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
    @DisplayName("save returns status 401 Unauthorized when user is not authenticated")
    void save_ReturnsStatus401Unauthorized_WhenUserIsNotAuthenticated() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/tags", HttpMethod.POST,
                null, Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }


    @Test
    @DisplayName("save returns status 401 Unauthorized when user is not editor")
    void save_ReturnsStatus401Unauthorized_WhenUserIsNotEditor() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/tags", HttpMethod.POST,
                new HttpEntity<>(null, httpHeadersWithNoRoleJwt), Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("save returns created tag response and status 201 Created when successful")
    void save_ReturnsCreatedTagResponseAndStatus201Created_WhenSuccessful() {
        TagCreateRequest tagCreateRequest = TagCreateRequestCreator.createValidTagCreateRequest();

        ResponseEntity<TagResponse> responseEntity = restTemplate.exchange("/tags", HttpMethod.POST,
                new HttpEntity<>(tagCreateRequest, httpHeadersWithRoleEditorJwt), TagResponse.class);

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

        ResponseEntity<ValidationErrorDetails> responseEntity = restTemplate.exchange("/tags", HttpMethod.POST,
                new HttpEntity<>(tagCreateRequest, httpHeadersWithRoleEditorJwt), ValidationErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ValidationErrorDetails.class);

        assertThat(responseEntity.getBody().getFields()).contains("name", "slug");
        log.info(String.format("Fields messages: %s", responseEntity.getBody().getFieldsMessages()));
    }

    @Test
    @DisplayName("save returns error details with JSON parse error and status 400 Bad Request when request body is " +
            "an invalid JSON")
    void save_ReturnsErrorDetailsWithJSONParseErrorAndStatus400BadRequest_WhenRequestBodyIsAnInvalidJSON() {
        String invalidJSON = "{ \"name\": \"news\"' }";

        httpHeadersWithRoleEditorJwt.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(invalidJSON, httpHeadersWithRoleEditorJwt);

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/tags", HttpMethod.POST,
                httpEntity, ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getTitle()).contains("JSON parse error");
    }

    @Test
    @DisplayName("save returns error details with constraint violation exception and status 400 Bad Request when " +
            "request name or slug already exists in the database")
    void save_ReturnsErrorDetailsWithConstraintViolationExceptionAndStatus400BadRequest_WhenNameOrSlugAlreadyExistsInTheDatabase() {
        tagRepository.save(TagCreator.createNewsTagToBeSaved());
        TagCreateRequest tagCreateRequest = TagCreateRequestCreator.createValidTagCreateRequest();

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/tags", HttpMethod.POST,
                new HttpEntity<>(tagCreateRequest, httpHeadersWithRoleEditorJwt), ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getTitle()).contains("Constraint Violation Exception");
        log.info(responseEntity.getBody().getMessage());
    }

    @Test
    @DisplayName("update returns status 401 Unauthorized when user is not authenticated")
    void update_ReturnsStatus401Unauthorized_WhenUserIsNotAuthenticated() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/tags/1", HttpMethod.PUT,
                null, Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update returns status 401 Unauthorized when user is not editor")
    void update_ReturnsStatus401Unauthorized_WhenUserIsNotEditor() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/tags/1", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithNoRoleJwt), Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update returns status 204 No Content when successful")
    void update_ReturnsStatus204NoContent_WhenSuccessful() {
        Tag savedTag = tagRepository.save(TagCreator.createNewsTagToBeSaved());
        TagUpdateRequest tagUpdateRequest = TagUpdateRequestCreator.createValidTagUpdateRequest();

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/tags/{id}", HttpMethod.PUT,
                new HttpEntity<>(tagUpdateRequest, httpHeadersWithRoleEditorJwt), Void.class, savedTag.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update returns error details and status 404 Not Found when tag is not found")
    void update_ReturnsErrorDetailsAndStatus404NotFound_WhenTagIsNotFound() {
        long tagId = 1;

        TagUpdateRequest tagUpdateRequest = TagUpdateRequestCreator.createValidTagUpdateRequest();

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/tags/{id}", HttpMethod.PUT,
                new HttpEntity<>(tagUpdateRequest, httpHeadersWithRoleEditorJwt), ErrorDetails.class, tagId);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/tags/" + tagId);

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Tag with identifier: " + tagId);
    }

    @Test
    @DisplayName("update returns validation error details and status 400 Bad Request when request body is invalid")
    void update_ReturnsValidationErrorDetailsAndStatus400BadRequest_WhenRequestBodyIsInvalid() {
        TagUpdateRequest tagUpdateRequest = TagUpdateRequestCreator.createInvalidTagUpdateRequest();

        ResponseEntity<ValidationErrorDetails> responseEntity = restTemplate.exchange("/tags/{id}", HttpMethod.PUT,
                new HttpEntity<>(tagUpdateRequest, httpHeadersWithRoleEditorJwt), ValidationErrorDetails.class, 1L);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ValidationErrorDetails.class);

        assertThat(responseEntity.getBody().getFields()).contains("name", "slug");
    }

    @Test
    @DisplayName("update returns error details with JSON parse error and status 400 Bad Request when request body " +
            "is an invalid JSON")
    void update_ReturnsErrorDetailsWithJSONParseErrorAndStatus400BadRequest_WhenRequestBodyIsAnInvalidJSON() {
        String invalidJSON = "{ \"name\": \"news\"' }";

        httpHeadersWithRoleEditorJwt.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(invalidJSON, httpHeadersWithRoleEditorJwt);

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/tags/{id}", HttpMethod.PUT,
                httpEntity, ErrorDetails.class, 1L);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getTitle()).contains("JSON parse error");
    }

    @Test
    @DisplayName("update returns error details with constraint violation exception and status 400 Bad Request when " +
            "request name or slug already exists in the database")
    void update_ReturnsErrorDetailsWithConstraintViolationExceptionAndStatus400BadRequest_WhenNameOrSlugAlreadyExistsInTheDatabase() {
        tagRepository.save(TagCreator.createTagToBeSaved("News"));
        Tag guidesTag = tagRepository.save(TagCreator.createTagToBeSaved("Guides"));

        TagUpdateRequest tagUpdateRequest = TagUpdateRequestCreator.createValidTagUpdateRequest("News");

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/tags/{id}", HttpMethod.PUT,
                new HttpEntity<>(tagUpdateRequest, httpHeadersWithRoleEditorJwt), ErrorDetails.class, guidesTag.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getTitle()).contains("Constraint Violation Exception");
    }

    @Test
    @DisplayName("update returns error details and status 400 Bad Request when id is not valid")
    void update_ReturnsErrorDetailsAndStatus400BadRequest_WhenIdIsNotValid() {
        tagRepository.save(TagCreator.createNewsTagToBeSaved());
        TagUpdateRequest tagUpdateRequest = TagUpdateRequestCreator.createValidTagUpdateRequest();

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/tags/invalidID", HttpMethod.PUT,
                new HttpEntity<>(tagUpdateRequest, httpHeadersWithRoleEditorJwt), ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/tags/invalidID");
    }

    @Test
    @DisplayName("deleteById returns status 401 Unauthorized when user is not authenticated")
    void deleteById_ReturnsStatus401Unauthorized_WhenUserIsNotAuthenticated() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/tags/1", HttpMethod.DELETE,
                null, Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("deleteById returns status 401 Unauthorized when user is not editor")
    void deleteById_ReturnsStatus401Unauthorized_WhenUserIsNotEditor() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/tags/1", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeadersWithNoRoleJwt), Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("deleteById returns status 204 No Content when successful")
    void deleteById_ReturnsStatus204NoContent_WhenSuccessful() {
        Tag savedTag = tagRepository.save(TagCreator.createNewsTagToBeSaved());

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/tags/{id}", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), Void.class, savedTag.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();

        ResponseEntity<ErrorDetails> findByIdResponseEntity = restTemplate.getForEntity("/tags/{id}",
                ErrorDetails.class, savedTag.getId());

        assertThat(findByIdResponseEntity).isNotNull();

        assertThat(findByIdResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("deleteById returns error details and status 404 Not Found when tag is not found")
    void deleteById_ReturnsErrorDetailsAndStatus404NotFound_WhenTagIsNotFound() {
        long tagId = 1;

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/tags/{id}", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class, tagId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/tags/" + tagId);

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Tag with identifier: " + tagId);
    }

    @Test
    @DisplayName("deleteById returns error details and status 400 Bad Request when id is not valid")
    void deleteById_ReturnsErrorDetailsAndStatus400BadRequest_WhenIdIsNotValid() {
        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/tags/invalidID", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/tags/invalidID");
    }
}
