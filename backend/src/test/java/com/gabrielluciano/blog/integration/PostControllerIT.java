package com.gabrielluciano.blog.integration;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.error.ErrorDetails;
import com.gabrielluciano.blog.error.ValidationErrorDetails;
import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.repositories.PostRepository;
import com.gabrielluciano.blog.repositories.TagRepository;
import com.gabrielluciano.blog.util.AuthUtil;
import com.gabrielluciano.blog.util.PostCreateRequestCreator;
import com.gabrielluciano.blog.util.PostCreator;
import com.gabrielluciano.blog.util.PostUpdateRequestCreator;
import com.gabrielluciano.blog.util.RegexPatterns;
import com.gabrielluciano.blog.util.TagCreator;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Log4j2
class PostControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

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
    @DisplayName("list returns page of post responses when successful")
    void list_ReturnsPageOfPostResponses_WhenSuccessful() {
        Post expectedPost = postRepository.save(PostCreator.createPublishedPostToBeSaved());

        ResponseEntity<RestPageImpl<PostResponse>> responseEntity = restTemplate.exchange("/posts", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(responseEntity.getBody().getContent().get(0).getId()).isEqualTo(expectedPost.getId());

        assertThat(responseEntity.getBody().getContent().get(0).getTitle()).isEqualTo(expectedPost.getTitle());
    }

    @Test
    @DisplayName("list returns empty page of post responses when no post is found")
    void list_ReturnsEmptyPageOfPostResponses_WhenNoPostIsFound() {
        postRepository.save(PostCreator.createUnpublishedPost());

        ResponseEntity<RestPageImpl<PostResponse>> responseEntity = restTemplate.exchange("/posts", HttpMethod.GET,
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
    @DisplayName("list returns page of post responses containing a specific title when successful")
    void list_ReturnsPageOfPostResponsesContainingASpecificTitle_WhenSuccessful() {
        String titleToBeFound1 = "Java Tools";
        String titleToBeFound2 = "Java tooling";
        String titleToBeNotFound = "Last news about javascript";
        String search = "tool";

        Post expectedPost1 = postRepository.save(PostCreator
                .createPublishedPostWithTitleAndSlugToBeSaved(titleToBeFound1, "some-slug-1"));
        Post expectedPost2 = postRepository.save(PostCreator
                .createPublishedPostWithTitleAndSlugToBeSaved(titleToBeFound2, "some-slug-2"));
        Post savedPost = postRepository.save(PostCreator
                .createPublishedPostWithTitleAndSlugToBeSaved(titleToBeNotFound, "some-slug-3"));

        ResponseEntity<RestPageImpl<PostResponse>> responseEntity = restTemplate.exchange("/posts?title={title}",
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                }, search);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        List<Long> ids = responseEntity.getBody().getContent()
                .stream()
                .map(PostResponse::getId)
                .toList();

        assertThat(ids)
                .contains(expectedPost1.getId(), expectedPost2.getId())
                .doesNotContain(savedPost.getId());
    }

    @Test
    @DisplayName("list returns status 401 Unauthorized when drafts is true and user is not authenticated")
    void list_ReturnsStatus401Unauthorized_WhenDraftsIsTrueAndUserIsNotAuthenticated() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts?drafts=true", HttpMethod.GET,
                null, Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("list returns status 401 Unauthorized when drafts is true and user is not editor")
    void list_ReturnsStatus401Unauthorized_WhenDraftsIsTrueAndUserIsNotEditor() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts?drafts=true", HttpMethod.GET,
                new HttpEntity<>(null, httpHeadersWithNoRoleJwt), Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("list returns page of unpublished post responses when drafts is true and user is editor")
    void list_ReturnsPageOfUnpublishedPostResponses_WhenDraftsIsTrueAndUserIsEditor() {
        postRepository.save(PostCreator.createPublishedPostToBeSaved());
        Post expectedPost = postRepository.save(PostCreator.createUnpublishedPostToBeSaved());

        ResponseEntity<RestPageImpl<PostResponse>> responseEntity = restTemplate.exchange("/posts?drafts={drafts}",
                HttpMethod.GET, new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), new ParameterizedTypeReference<>() {
                }, true);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(responseEntity.getBody().getContent().get(0).getId()).isEqualTo(expectedPost.getId());

        assertThat(responseEntity.getBody().getContent().get(0).getTitle()).isEqualTo(expectedPost.getTitle());

        assertThat(responseEntity.getBody().getContent().get(0).getPublished()).isFalse();
    }

    @Test
    @DisplayName("list returns page of post responses containing a specific tag response when tag parameter is passed")
    void list_ReturnsPageOfPostResponsesContainingASpecificTagResponse_WhenTagParameterIsPassed() {
        Tag savedTag = tagRepository.save(TagCreator.createTagToBeSaved("development"));
        postRepository.save(PostCreator.createPublishedPostWithTagsToBeSaved(Set.of(savedTag)));

        Tag expectedTag = tagRepository.save(TagCreator.createTagToBeSaved("news"));
        Post expectedPost = postRepository.save(PostCreator.createPublishedPostWithTagsToBeSaved(Set.of(expectedTag)));

        ResponseEntity<RestPageImpl<PostResponse>> responseEntity = restTemplate.exchange("/posts?tag={tagId}",
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                }, expectedTag.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(responseEntity.getBody().getContent().get(0).getId()).isEqualTo(expectedPost.getId());

        assertThat(responseEntity.getBody().getContent().get(0).getTitle()).isEqualTo(expectedPost.getTitle());

        assertThat(responseEntity.getBody().getContent().get(0).getTags()).hasSize(1);
    }

    @Test
    @DisplayName("list returns page of post responses containing a specific title and tag response when title and tag parameters are passed")
    void list_ReturnsPageOfPostResponsesContainingASpecificTitleAndTagResponse_WhenTitleAndTagParametersArePassed() {
        String expectedTitle = "Java Tools";
        String savedTitle1 = "Java tooling";
        String savedTitle2 = "Some title";
        String titleSearch = "tool";

        Tag expectedTag = tagRepository.save(TagCreator.createTagToBeSaved("development"));
        Tag savedTag = tagRepository.save(TagCreator.createTagToBeSaved("news"));

        Post expectedPost = postRepository.save(PostCreator
                .createPostToBeSaved(expectedTitle, "s1", true, Set.of(expectedTag)));
        postRepository.save(PostCreator.createPostToBeSaved(savedTitle1, "s2", true, Set.of(savedTag)));
        postRepository.save(PostCreator.createPostToBeSaved(savedTitle2, "s3", true, Set.of(expectedTag)));

        ResponseEntity<RestPageImpl<PostResponse>> responseEntity = restTemplate.
                exchange("/posts?title={title}&tag={tagId}", HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {
                        }, titleSearch, expectedTag.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(responseEntity.getBody().getContent().get(0).getId()).isEqualTo(expectedPost.getId());

        assertThat(responseEntity.getBody().getContent().get(0).getTitle()).isEqualTo(expectedTitle);

        assertThat(responseEntity.getBody().getContent().get(0).getTags()).hasSize(1);
    }

    @Test
    @DisplayName("findBySlug returns published post response when and user is not authenticated")
    void findBySlug_ReturnsPublishedPostResponse_WhenSuccessfulAndUserIsNotAuthenticated() {
        String expectedSlug = "some-post";

        Post expectedPost = postRepository.save(PostCreator
                .createPublishedPostWithTitleAndSlugToBeSaved("Expected Title", expectedSlug));

        ResponseEntity<PostResponse> responseEntity = restTemplate
                .getForEntity("/posts/slug/{slug}", PostResponse.class, expectedSlug);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getId())
                .isEqualTo(expectedPost.getId());

        assertThat(responseEntity.getBody().getTitle())
                .isEqualTo(expectedPost.getTitle());
    }

    @Test
    @DisplayName("findBySlug returns published post response when successful and user is editor")
    void findBySlug_ReturnsPublishedPostResponse_WhenSuccessfulAndUserIsEditor() {
        String expectedSlug = "some-post";

        Post expectedPost = postRepository.save(PostCreator
                .createPublishedPostWithTitleAndSlugToBeSaved("Expected Title", expectedSlug));

        ResponseEntity<PostResponse> responseEntity = restTemplate.exchange("/posts/slug/{slug}", HttpMethod.GET,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), PostResponse.class, expectedSlug);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getId())
                .isEqualTo(expectedPost.getId());

        assertThat(responseEntity.getBody().getTitle())
                .isEqualTo(expectedPost.getTitle());
    }

    @Test
    @DisplayName("findBySlug returns error details and status 404 Not Found when post is not published and user is not authenticated")
    void findBySlug_ReturnsErrorDetailsAndStatus404NotFound_WhenPostIsNotPublishedAndUserIsNotAuthenticated() {
        String expectedSlug = "some-post";

        postRepository.save(PostCreator.createUnpublishedPostWithTitleAndSlugToBeSaved("Expected Title", expectedSlug));

        ResponseEntity<ErrorDetails> responseEntity = restTemplate
                .getForEntity("/posts/slug/{slug}", ErrorDetails.class, expectedSlug);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/slug/" + expectedSlug);

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Post with identifier: " + expectedSlug);
    }

    @Test
    @DisplayName("findBySlug returns unpublished post response when successful and user is editor")
    void findBySlug_ReturnsUnpublishedPostResponse_WhenSuccessfulAndUserIsEditor() {
        String expectedSlug = "some-post";

        Post expectedPost = postRepository.save(PostCreator
                .createUnpublishedPostWithTitleAndSlugToBeSaved("Expected Title", expectedSlug));

        ResponseEntity<PostResponse> responseEntity = restTemplate.exchange("/posts/slug/{slug}", HttpMethod.GET,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), PostResponse.class, expectedSlug);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getId())
                .isEqualTo(expectedPost.getId());

        assertThat(responseEntity.getBody().getTitle())
                .isEqualTo(expectedPost.getTitle());
    }

    @Test
    @DisplayName("findBySlug returns error details and status 404 Not Found when post is not found")
    void findBySlug_ReturnsErrorDetailsAndStatus404NotFound_WhenPostIsNotFound() {
        String expectedSlug = "some-post";

        ResponseEntity<ErrorDetails> responseEntity = restTemplate
                .getForEntity("/posts/slug/{slug}", ErrorDetails.class, expectedSlug);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/slug/" + expectedSlug);

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Post with identifier: " + expectedSlug);
    }

    @Test
    @DisplayName("save returns status 401 Unauthorized when user is not authenticated")
    void save_ReturnsStatus401Unauthorized_WhenUserIsNotAuthenticated() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts", HttpMethod.POST,
                null, Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("save returns status 401 Unauthorized when user is not editor")
    void save_ReturnsStatus401Unauthorized_WhenUserIsNotEditor() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts", HttpMethod.POST,
                new HttpEntity<>(null, httpHeadersWithNoRoleJwt), Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("save returns created post response and status 201 Created when successful")
    void save_ReturnsCreatedPostResponseAndStatus201Created_WhenSuccessful() {
        PostCreateRequest postCreateRequest = PostCreateRequestCreator.createValidPostCreateRequest();

        ResponseEntity<PostResponse> responseEntity = restTemplate.exchange("/posts", HttpMethod.POST,
                new HttpEntity<>(postCreateRequest, httpHeadersWithRoleEditorJwt), PostResponse.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        PostResponse postResponse = responseEntity.getBody();

        assertThat(postResponse).isNotNull();

        assertThat(postResponse.getId()).isNotNull();

        assertThat(postResponse.getTitle()).isEqualTo(postCreateRequest.getTitle());

        assertThat(postResponse.getSlug())
                .isNotNull()
                .matches(RegexPatterns.VALID_SLUG_PATTERN)
                .isEqualTo(postCreateRequest.getSlug());

        assertThat(postResponse.getCreatedAt()).isNotNull();

        assertThat(postResponse.getUpdatedAt()).isNotNull();

        assertThat(postResponse.getPublishedAt()).isNull();

        assertThat(postResponse.getPublished()).isFalse();

        assertThat(postResponse.getTags())
                .isNotNull()
                .isEmpty();

        assertThat(postResponse.getAuthor()).isNotNull();
    }

    @Test
    @DisplayName("save returns validation error details and status 400 Bad Request when request body contains an invalid title")
    void save_ReturnsValidationErrorDetailsAndStatus400BadRequest_WhenRequestBodyContainsAnInvalidTitle() {
        PostCreateRequest postCreateRequest = PostCreateRequestCreator.createPostCreateRequestWithTitle("");

        ResponseEntity<ValidationErrorDetails> responseEntity = restTemplate.exchange("/posts", HttpMethod.POST,
                new HttpEntity<>(postCreateRequest, httpHeadersWithRoleEditorJwt), ValidationErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ValidationErrorDetails.class);

        assertThat(responseEntity.getBody().getFields()).isEqualTo("title");

        log.info(String.format("Fields messages: %s", responseEntity.getBody().getFieldsMessages()));
    }

    @Test
    @DisplayName("save returns validation error details and status 400 Bad Request when request body contains an invalid slug")
    void save_ReturnsValidationErrorDetailsAndStatus400BadRequest_WhenRequestBodyContainsAnInvalidSlug() {
        tryToSavePostWithInvalidSlugAndValidateThatValidationErrorDetailsIsReturned("invalid slug");
        tryToSavePostWithInvalidSlugAndValidateThatValidationErrorDetailsIsReturned("-invalid-slug");
        tryToSavePostWithInvalidSlugAndValidateThatValidationErrorDetailsIsReturned("´invalid-slug");
        tryToSavePostWithInvalidSlugAndValidateThatValidationErrorDetailsIsReturned("çinvalid-slug");
    }

    private void tryToSavePostWithInvalidSlugAndValidateThatValidationErrorDetailsIsReturned(String slug) {
        PostCreateRequest postCreateRequest = PostCreateRequestCreator.createPostCreateRequestWithSlug(slug);

        ResponseEntity<ValidationErrorDetails> responseEntity = restTemplate.exchange("/posts", HttpMethod.POST,
                new HttpEntity<>(postCreateRequest, httpHeadersWithRoleEditorJwt), ValidationErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ValidationErrorDetails.class);

        assertThat(responseEntity.getBody().getFields()).isEqualTo("slug");

        log.info(String.format("Fields messages: %s", responseEntity.getBody().getFieldsMessages()));
    }

    @Test
    @DisplayName("save returns error details with constraint violation exception and status 400 Bad Request when " +
            "request title or slug already exists in the database")
    void save_ReturnsErrorDetailsWithConstraintViolationExceptionAndStatus400BadRequest_WhenTitleOrSlugAlreadyExistsInTheDatabase() {
        postRepository.save(PostCreator.createPublishedPost());
        PostCreateRequest postCreateRequest = PostCreateRequestCreator.createValidPostCreateRequest();

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts", HttpMethod.POST,
                new HttpEntity<>(postCreateRequest, httpHeadersWithRoleEditorJwt), ErrorDetails.class);

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
    @DisplayName("save returns error details with JSON parse error and status 400 Bad Request when request body " +
            "is an invalid JSON")
    void save_ReturnsErrorDetailsWithJSONParseErrorAndStatus400BadRequest_WhenRequestBodyIsAnInvalidJSON() {
        String invalidJSON = "{ \"name\": \"news\"' }";

        httpHeadersWithRoleEditorJwt.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(invalidJSON, httpHeadersWithRoleEditorJwt);

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts", HttpMethod.POST,
                httpEntity, ErrorDetails.class, 1L);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getTitle()).contains("JSON parse error");
    }

    @Test
    @DisplayName("update returns status 401 Unauthorized when user is not authenticated")
    void update_ReturnsStatus401Unauthorized_WhenUserIsNotAuthenticated() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/1", HttpMethod.PUT,
                null, Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update returns status 401 Unauthorized when user is not editor")
    void update_ReturnsStatus401Unauthorized_WhenUserIsNotEditor() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/1", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithNoRoleJwt), Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update returns status 204 No Content when successful")
    void update_ReturnsStatus204NoContent_WhenSuccessful() {
        Post savedPost = postRepository.save(PostCreator.createPublishedPostToBeSaved());
        LocalDateTime updatedAtBefore = savedPost.getUpdatedAt();

        PostUpdateRequest postUpdateRequest = PostUpdateRequestCreator.createValidPostUpdateRequest();

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/{id}", HttpMethod.PUT,
                new HttpEntity<>(postUpdateRequest, httpHeadersWithRoleEditorJwt), Void.class, savedPost.getId());

        try {
            savedPost = postRepository.findById(savedPost.getId()).orElseThrow();
        } catch (Exception e) {
            log.error("Error fetching saved post");
        }

        assertThat(savedPost.getUpdatedAt()).isAfter(updatedAtBefore);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("update returns error details and status 404 Not Found when post is not found")
    void update_ReturnsErrorDetailsAndStatus404NotFound_WhenPostIsNotFound() {
        long postId = 1;

        PostUpdateRequest postUpdateRequest = PostUpdateRequestCreator.createValidPostUpdateRequest();

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/{id}", HttpMethod.PUT,
                new HttpEntity<>(postUpdateRequest, httpHeadersWithRoleEditorJwt), ErrorDetails.class, postId);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/" + postId);

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Post with identifier: " + postId);
    }

    @Test
    @DisplayName("update returns validation error details and status 400 Bad Request when request body contains an invalid title")
    void update_ReturnsValidationErrorDetailsAndStatus400BadRequest_WhenRequestBodyContainsAnInvalidTitle() {
        PostUpdateRequest postUpdateRequest = PostUpdateRequestCreator.createValidPostUpdateRequestWithTitle("");

        ResponseEntity<ValidationErrorDetails> responseEntity = restTemplate.exchange("/posts/9", HttpMethod.PUT,
                new HttpEntity<>(postUpdateRequest, httpHeadersWithRoleEditorJwt), ValidationErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ValidationErrorDetails.class);

        assertThat(responseEntity.getBody().getFields()).isEqualTo("title");

        log.info(String.format("Fields messages: %s", responseEntity.getBody().getFieldsMessages()));
    }

    @Test
    @DisplayName("update returns validation error details and status 400 Bad Request when request body contains an invalid slug")
    void update_ReturnsValidationErrorDetailsAndStatus400BadRequest_WhenRequestBodyContainsAnInvalidSlug() {
        tryToUpdatePostWithInvalidSlugAndValidateThatValidationErrorDetailsIsReturned("invalid slug");
        tryToUpdatePostWithInvalidSlugAndValidateThatValidationErrorDetailsIsReturned("-invalid-slug");
        tryToUpdatePostWithInvalidSlugAndValidateThatValidationErrorDetailsIsReturned("´invalid-slug");
        tryToUpdatePostWithInvalidSlugAndValidateThatValidationErrorDetailsIsReturned("çinvalid-slug");
    }

    private void tryToUpdatePostWithInvalidSlugAndValidateThatValidationErrorDetailsIsReturned(String slug) {
        PostUpdateRequest postUpdateRequest = PostUpdateRequestCreator.createValidPostUpdateRequestWithSlug(slug);

        ResponseEntity<ValidationErrorDetails> responseEntity = restTemplate.exchange("/posts/9", HttpMethod.PUT,
                new HttpEntity<>(postUpdateRequest, httpHeadersWithRoleEditorJwt), ValidationErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ValidationErrorDetails.class);

        assertThat(responseEntity.getBody().getFields()).isEqualTo("slug");

        log.info(String.format("Fields messages: %s", responseEntity.getBody().getFieldsMessages()));
    }

    @Test
    @DisplayName("update returns error details with JSON parse error and status 400 Bad Request when request body " +
            "is an invalid JSON")
    void update_ReturnsErrorDetailsWithJSONParseErrorAndStatus400BadRequest_WhenRequestBodyIsAnInvalidJSON() {
        String invalidJSON = "{ \"name\": \"news\"' }";

        httpHeadersWithRoleEditorJwt.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(invalidJSON, httpHeadersWithRoleEditorJwt);

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/{id}", HttpMethod.PUT,
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
            "request title or slug already exists in the database")
    void update_ReturnsErrorDetailsWithConstraintViolationExceptionAndStatus400BadRequest_WhenTitleOrSlugAlreadyExistsInTheDatabase() {
        postRepository.save(PostCreator
                .createPublishedPostWithTitleAndSlugToBeSaved("Spring Boot", "spring-boot"));
        Post javaPost = postRepository.save(PostCreator
                .createPublishedPostWithTitleAndSlugToBeSaved("Java", "java"));

        PostUpdateRequest postUpdateRequest = PostUpdateRequestCreator
                .createValidPostUpdateRequestWithTitleAndSlug("Spring Boot", "spring-boot");

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/{id}", HttpMethod.PUT,
                new HttpEntity<>(postUpdateRequest, httpHeadersWithRoleEditorJwt), ErrorDetails.class, javaPost.getId());

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
        postRepository.save(PostCreator.createPublishedPostToBeSaved());
        PostUpdateRequest postUpdateRequest = PostUpdateRequestCreator.createValidPostUpdateRequest();

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/invalidID", HttpMethod.PUT,
                new HttpEntity<>(postUpdateRequest, httpHeadersWithRoleEditorJwt), ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/invalidID");
    }

    @Test
    @DisplayName("deleteById returns status 401 Unauthorized when user is not authenticated")
    void deleteById_ReturnsStatus401Unauthorized_WhenUserIsNotAuthenticated() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/1", HttpMethod.DELETE,
                null, Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("deleteById returns status 401 Unauthorized when user is not editor")
    void deleteById_ReturnsStatus401Unauthorized_WhenUserIsNotEditor() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/1", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeadersWithNoRoleJwt), Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("deleteById returns status 204 No Content when successful")
    void deleteById_ReturnsStatus204NoContent_WhenSuccessful() {
        Post savedPost = postRepository.save(PostCreator.createPublishedPostToBeSaved());

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/{id}", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), Void.class, savedPost.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();

        ResponseEntity<ErrorDetails> findBySlugResponseEntity = restTemplate.exchange("/posts/slug/{slug}?drafts=true",
                HttpMethod.GET, new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class, savedPost.getSlug());

        assertThat(findBySlugResponseEntity).isNotNull();

        assertThat(findBySlugResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    @DisplayName("deleteById returns error details and status 404 Not Found when post is not found")
    void deleteById_ReturnsErrorDetailsAndStatus404NotFound_WhenPostIsNotFound() {
        long postId = 1;

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/{id}", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class, postId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/" + postId);

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Post with identifier: " + postId);
    }

    @Test
    @DisplayName("deleteById returns error details and status 400 Bad Request when id is not valid")
    void deleteById_ReturnsErrorDetailsAndStatus400BadRequest_WhenIdIsNotValid() {
        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/invalidID", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/invalidID");
    }

    @Test
    @DisplayName("addTag returns status 401 Unauthorized when user is not authenticated")
    void addTag_ReturnsStatus401Unauthorized_WhenUserIsNotAuthenticated() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/1/tags/1", HttpMethod.PUT,
                null, Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("addTag returns status 401 Unauthorized when user is not editor")
    void addTag_ReturnsStatus401Unauthorized_WhenUserIsNotEditor() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/1/tags/1", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithNoRoleJwt), Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("addTag returns status 204 No Content when successful")
    void addTag_ReturnsStatus204NoContent_WhenSuccessful() {
        Post savedPost = postRepository.save(PostCreator.createPublishedPostToBeSaved());
        Tag savedTag = tagRepository.save(TagCreator.createTagToBeSaved("Some tag"));

        assertThat(savedPost.getTags())
                .isNotNull()
                .isEmpty();

        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange("/posts/{postId}/tags/{tagId}", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), Void.class, savedPost.getId(), savedTag.getId());

        assertThat(voidResponseEntity).isNotNull();

        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(voidResponseEntity.getBody()).isNull();

        ResponseEntity<PostResponse> postResponseEntity = restTemplate.getForEntity("/posts/slug/{slug}",
                PostResponse.class, savedPost.getSlug());

        assertThat(postResponseEntity).isNotNull();

        assertThat(postResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(postResponseEntity.getBody()).isNotNull();

        assertThat(postResponseEntity.getBody().getTags())
                .isNotNull()
                .hasSize(1);
    }

    @Test
    @DisplayName("addTag returns error details and status 404 Not Found when post is not found")
    void addTag_ReturnsErrorDetailsAndStatus404NotFound_WhenPostIsNotFound() {
        long postId = 1;
        Tag savedTag = tagRepository.save(TagCreator.createTagToBeSaved("Some tag"));

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/{postId}/tags/{tagId}", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class, postId, savedTag.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath())
                .isEqualTo(String.format("/posts/%s/tags/%s", postId, savedTag.getId()));

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Post with identifier: " + postId);
    }

    @Test
    @DisplayName("addTag returns error details and status 404 Not Found when tag is not found")
    void addTag_ReturnsErrorDetailsAndStatus404NotFound_WhenTagIsNotFound() {
        Post savedPost = postRepository.save(PostCreator.createPublishedPostToBeSaved());
        long tagId = 1;

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/{postId}/tags/{tagId}", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class, savedPost.getId(), tagId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath())
                .isEqualTo(String.format("/posts/%s/tags/%s", savedPost.getId(), tagId));

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Tag with identifier: " + tagId);
    }

    @Test
    @DisplayName("addTag returns error details and status 400 Bad Request when postId is not valid")
    void addTag_ReturnsErrorDetailsAndStatus400BadRequest_WhenPostIdIsNotValid() {
        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/invalidID/tags/1", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/invalidID/tags/1");
    }

    @Test
    @DisplayName("addTag returns error details and status 400 Bad Request when tagId is not valid")
    void addTag_ReturnsErrorDetailsAndStatus400BadRequest_WhenTagIdIsNotValid() {
        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/1/tags/invalidID", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/1/tags/invalidID");
    }

    @Test
    @DisplayName("removeTag returns status 401 Unauthorized when user is not authenticated")
    void removeTag_ReturnsStatus401Unauthorized_WhenUserIsNotAuthenticated() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/1/tags/1", HttpMethod.DELETE,
                null, Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("removeTag returns status 401 Unauthorized when user is not editor")
    void removeTag_ReturnsStatus401Unauthorized_WhenUserIsNotEditor() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/1/tags/1", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeadersWithNoRoleJwt), Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("removeTag returns status 204 No Content when successful")
    void removeTag_ReturnsStatus204NoContent_WhenSuccessful() {
        Tag savedTag = tagRepository.save(TagCreator.createTagToBeSaved("Some tag"));
        Post savedPost = postRepository.save(PostCreator.createPublishedPostWithTagsToBeSaved(Set.of(savedTag)));

        assertThat(savedPost.getTags())
                .isNotNull()
                .hasSize(1);

        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange("/posts/{postId}/tags/{tagId}", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), Void.class, savedPost.getId(), savedTag.getId());

        assertThat(voidResponseEntity).isNotNull();

        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(voidResponseEntity.getBody()).isNull();

        ResponseEntity<PostResponse> postResponseEntity = restTemplate.getForEntity("/posts/slug/{slug}",
                PostResponse.class, savedPost.getSlug());

        assertThat(postResponseEntity).isNotNull();

        assertThat(postResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(postResponseEntity.getBody()).isNotNull();

        assertThat(postResponseEntity.getBody().getTags())
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("removeTag returns error details and status 404 Not Found when post is not found")
    void removeTag_ReturnsErrorDetailsAndStatus404NotFound_WhenPostIsNotFound() {
        long postId = 1;
        Tag savedTag = tagRepository.save(TagCreator.createTagToBeSaved("Some tag"));

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/{postId}/tags/{tagId}", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class, postId, savedTag.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath())
                .isEqualTo(String.format("/posts/%s/tags/%s", postId, savedTag.getId()));

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Post with identifier: " + postId);
    }

    @Test
    @DisplayName("removeTag returns error details and status 404 Not Found when tag is not found")
    void removeTag_ReturnsErrorDetailsAndStatus404NotFound_WhenTagIsNotFound() {
        Post savedPost = postRepository.save(PostCreator.createPublishedPostToBeSaved());
        long tagId = 1;

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/{postId}/tags/{tagId}", HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class, savedPost.getId(), tagId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath())
                .isEqualTo(String.format("/posts/%s/tags/%s", savedPost.getId(), tagId));

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Tag with identifier: " + tagId);
    }

    @Test
    @DisplayName("removeTag returns error details and status 400 Bad Request when postId is not valid")
    void removeTag_ReturnsErrorDetailsAndStatus400BadRequest_WhenPostIdIsNotValid() {
        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/invalidID/tags/1",
                HttpMethod.DELETE, new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/invalidID/tags/1");
    }

    @Test
    @DisplayName("removeTag returns error details and status 400 Bad Request when tagId is not valid")
    void removeTag_ReturnsErrorDetailsAndStatus400BadRequest_WhenTagIdIsNotValid() {
        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/1/tags/invalidID",
                HttpMethod.DELETE, new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/1/tags/invalidID");
    }

    @Test
    @DisplayName("publishById returns status 401 Unauthorized when user is not authenticated")
    void publishById_ReturnsStatus401Unauthorized_WhenUserIsNotAuthenticated() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/1/publish", HttpMethod.PUT,
                null, Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("publishById returns status 401 Unauthorized when user is not editor")
    void publishById_ReturnsStatus401Unauthorized_WhenUserIsNotEditor() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/1/publish", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithNoRoleJwt), Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("publishById returns status 204 No Content when successful")
    void publishById_ReturnsStatus204NoContent_WhenSuccessful() {
        Post savedPost = postRepository.save(PostCreator.createUnpublishedPostToBeSaved());

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/{id}/publish", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), Void.class, savedPost.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();

        ResponseEntity<PostResponse> findBySlugResponseEntity = restTemplate.getForEntity("/posts/slug/{slug}",
                PostResponse.class, savedPost.getSlug());

        assertThat(findBySlugResponseEntity).isNotNull();

        assertThat(findBySlugResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(findBySlugResponseEntity.getBody()).isNotNull();

        assertThat(findBySlugResponseEntity.getBody().getTitle()).isEqualTo(savedPost.getTitle());

        assertThat(savedPost.getPublished()).isFalse();

        assertThat(findBySlugResponseEntity.getBody().getPublished()).isTrue();

        assertThat(findBySlugResponseEntity.getBody().getPublishedAt())
                .isNotNull()
                .isAfter(findBySlugResponseEntity.getBody().getCreatedAt());
    }

    @Test
    @DisplayName("publishById returns error details and status 404 Not Found when post is not found")
    void publishById_ReturnsErrorDetailsAndStatus404NotFound_WhenPostIsNotFound() {
        long postId = 1;

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/{id}/publish", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class, postId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/" + postId + "/publish");

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Post with identifier: " + postId);
    }

    @Test
    @DisplayName("publishById returns error details and status 400 Bad Request when id is not valid")
    void publishById_ReturnsErrorDetailsAndStatus400BadRequest_WhenIdIsNotValid() {
        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/invalidID/publish", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/invalidID/publish");
    }

    @Test
    @DisplayName("unpublishById returns status 401 Unauthorized when user is not authenticated")
    void unpublishById_ReturnsStatus401Unauthorized_WhenUserIsNotAuthenticated() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/1/unpublish", HttpMethod.PUT,
                null, Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("unpublishById returns status 401 Unauthorized when user is not editor")
    void unpublishById_ReturnsStatus401Unauthorized_WhenUserIsNotEditor() {
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/1/unpublish", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithNoRoleJwt), Void.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("unpublishById returns status 204 No Content when successful")
    void unpublishById_ReturnsStatus204NoContent_WhenSuccessful() {
        Post savedPost = postRepository.save(PostCreator.createPublishedPostToBeSaved());

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/posts/{id}/unpublish", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), Void.class, savedPost.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(responseEntity.getBody()).isNull();

        ResponseEntity<PostResponse> findBySlugResponseEntity = restTemplate.exchange("/posts/slug/{slug}?drafts=true",
                HttpMethod.GET, new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), PostResponse.class, savedPost.getSlug());

        assertThat(findBySlugResponseEntity).isNotNull();

        assertThat(findBySlugResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(findBySlugResponseEntity.getBody()).isNotNull();

        assertThat(findBySlugResponseEntity.getBody().getTitle()).isEqualTo(savedPost.getTitle());

        assertThat(savedPost.getPublished()).isTrue();

        assertThat(findBySlugResponseEntity.getBody().getPublished()).isFalse();

        assertThat(findBySlugResponseEntity.getBody().getPublishedAt()).isNull();
    }

    @Test
    @DisplayName("unpublishById returns error details and status 404 Not Found when post is not found")
    void unpublishById_ReturnsErrorDetailsAndStatus404NotFound_WhenPostIsNotFound() {
        long postId = 1;

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/{id}/unpublish", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class, postId);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/" + postId + "/unpublish");

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Could not find resource of type Post with identifier: " + postId);
    }

    @Test
    @DisplayName("unpublishById returns error details and status 400 Bad Request when id is not valid")
    void unpublishById_ReturnsErrorDetailsAndStatus400BadRequest_WhenIdIsNotValid() {
        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/posts/invalidID/unpublish", HttpMethod.PUT,
                new HttpEntity<>(null, httpHeadersWithRoleEditorJwt), ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/invalidID/unpublish");
    }
}
