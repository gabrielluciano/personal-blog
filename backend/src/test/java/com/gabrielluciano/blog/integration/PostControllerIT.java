package com.gabrielluciano.blog.integration;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.error.ErrorDetails;
import com.gabrielluciano.blog.error.ValidationErrorDetails;
import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.repositories.PostRepository;
import com.gabrielluciano.blog.repositories.TagRepository;
import com.gabrielluciano.blog.util.PostCreateRequestCreator;
import com.gabrielluciano.blog.util.PostCreator;
import com.gabrielluciano.blog.util.RegexPatterns;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Log4j2
class PostControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("list returns page of post responses when successful")
    void list_ReturnsPageOfPostResponses_WhenSuccessful() {
        Post expectedPost = postRepository.save(PostCreator.createPublishedPostToBeSaved());

        ResponseEntity<RestPageImpl<PostResponse>> responseEntity = restTemplate.exchange("/posts", HttpMethod.GET,
                 null, new ParameterizedTypeReference<>() {});

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
                 null, new ParameterizedTypeReference<>() {});

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
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {}, search);

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
    @DisplayName("list returns page of unpublished post responses when drafts is true")
    void list_ReturnsPageOfUnpublishedPostResponses_WhenDraftsIsTrue() {
        postRepository.save(PostCreator.createPublishedPostToBeSaved());
        Post expectedPost = postRepository.save(PostCreator.createUnpublishedPostToBeSaved());

        ResponseEntity<RestPageImpl<PostResponse>> responseEntity = restTemplate.exchange("/posts?drafts={drafts}",
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {}, true);

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
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {}, expectedTag.getId());

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
                        new ParameterizedTypeReference<>() {}, titleSearch, expectedTag.getId());

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
    @DisplayName("findById returns post response when successful")
    void findById_ReturnsPostResponse_WhenSuccessful() {
        Post savedPost = postRepository.save(PostCreator.createPublishedPostToBeSaved());

        ResponseEntity<PostResponse> responseEntity = restTemplate
                .getForEntity("/posts/{id}", PostResponse.class, savedPost.getId());

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getId())
                .isEqualTo(savedPost.getId());

        assertThat(responseEntity.getBody().getTitle())
                .isEqualTo(savedPost.getTitle());
    }

    @Test
    @DisplayName("findById returns error details and status 404 Not Found when post is not found")
    void findById_ReturnsErrorDetailsAndStatus404NotFound_WhenPostIsNotFound() {
        long postId = 1;

        ResponseEntity<ErrorDetails> responseEntity = restTemplate
                .getForEntity("/posts/{id}", ErrorDetails.class, postId);

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
    @DisplayName("findById returns error details and status 400 Bad Request when id is not valid")
    void findById_ReturnsErrorDetailsAndStatus400BadRequest_WhenIdIsNotValid() {
        ResponseEntity<ErrorDetails> responseEntity = restTemplate
                .getForEntity("/posts/invalidID", ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/posts/invalidID");
    }

    @Test
    @DisplayName("findBySlug returns post response when successful")
    void findBySlug_ReturnsPostResponse_WhenSuccessful() {
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
    @DisplayName("save returns created post response and status 201 Created when successful")
    void save_ReturnsCreatedPostResponseAndStatus201Created_WhenSuccessful() {
        PostCreateRequest postCreateRequest = PostCreateRequestCreator.createValidPostCreateRequest();

        ResponseEntity<PostResponse> responseEntity = restTemplate
                .postForEntity("/posts", postCreateRequest, PostResponse.class);

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
    }

    @Test
    @DisplayName("save returns validation error details and status 400 Bad Request when request body contains an invalid title")
    void save_ReturnsValidationErrorDetailsAndStatus400BadRequest_WhenRequestBodyContainsAnInvalidTitle() {
        PostCreateRequest postCreateRequest = PostCreateRequestCreator.createPostCreateRequestWithTitle("");

        ResponseEntity<ValidationErrorDetails> responseEntity = restTemplate
                .postForEntity("/posts", postCreateRequest, ValidationErrorDetails.class);

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

        ResponseEntity<ValidationErrorDetails> responseEntity = restTemplate
                .postForEntity("/posts", postCreateRequest, ValidationErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ValidationErrorDetails.class);

        assertThat(responseEntity.getBody().getFields()).isEqualTo("slug");

        log.info(String.format("Fields messages: %s", responseEntity.getBody().getFieldsMessages()));
    }
}
