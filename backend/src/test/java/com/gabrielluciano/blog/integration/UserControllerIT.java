package com.gabrielluciano.blog.integration;

import com.gabrielluciano.blog.dto.user.LoginRequest;
import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.error.ErrorDetails;
import com.gabrielluciano.blog.error.ValidationErrorDetails;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.repositories.UserRepository;
import com.gabrielluciano.blog.util.LoginRequestCreator;
import com.gabrielluciano.blog.util.TestRegexPatterns;
import com.gabrielluciano.blog.util.UserCreateRequestCreator;
import com.gabrielluciano.blog.util.UserCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
class UserControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("signup returns UserResponse and status 201 Created when successful")
    void signup_ReturnsUserResponseAndStatus201Created_WhenSuccessful() {
        UserCreateRequest userCreateRequest = UserCreateRequestCreator.createValidUserCreateRequest();

        ResponseEntity<UserResponse> responseEntity = restTemplate.postForEntity("/signup", userCreateRequest, UserResponse.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getName()).isEqualTo(userCreateRequest.getName());

        assertThat(responseEntity.getBody().getEmail()).isEqualTo(userCreateRequest.getEmail());

        assertThat(responseEntity.getBody().getRoles()).contains(Role.ADMIN);
    }

    @Test
    @DisplayName("signup returns ValidationErrorDetails and status 400 Bad Request when email is invalid")
    void signup_ReturnsValidationErrorDetailsAndStatus400BadRequest_WhenEmailIsInvalid() {
        tryToSignupUserWithInvalidEmailValidateThatValidationErrorDetailsIsReturned("invalid email");
        tryToSignupUserWithInvalidEmailValidateThatValidationErrorDetailsIsReturned("invalid@");
        tryToSignupUserWithInvalidEmailValidateThatValidationErrorDetailsIsReturned("@email.com");
        tryToSignupUserWithInvalidEmailValidateThatValidationErrorDetailsIsReturned("çàp@email.com");
        tryToSignupUserWithInvalidEmailValidateThatValidationErrorDetailsIsReturned("john@/email.com");
    }

    private void tryToSignupUserWithInvalidEmailValidateThatValidationErrorDetailsIsReturned(String email) {
        UserCreateRequest userCreateRequest = UserCreateRequestCreator.createUserCreateRequestWithEmail(email);

        ResponseEntity<ValidationErrorDetails> responseEntity = restTemplate.postForEntity("/signup",
                userCreateRequest, ValidationErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ValidationErrorDetails.class);

        assertThat(responseEntity.getBody().getFields()).isEqualTo("email");

        log.info(String.format("Fields messages: %s", responseEntity.getBody().getFieldsMessages()));
    }

    @Test
    @DisplayName("signup returns ValidationErrorDetails and status 400 Bad Request when name is blank")
    void signup_ReturnsValidationErrorDetailsAndStatus400BadRequest_WhenNameIsBlank() {
        UserCreateRequest userCreateRequest = UserCreateRequestCreator.createUserCreateRequestWithName("");

        ResponseEntity<ValidationErrorDetails> responseEntity = restTemplate.postForEntity("/signup",
                userCreateRequest, ValidationErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ValidationErrorDetails.class);

        assertThat(responseEntity.getBody().getFields()).isEqualTo("name");

        log.info(String.format("Fields messages: %s", responseEntity.getBody().getFieldsMessages()));
    }

    @Test
    @DisplayName("signup returns ValidationErrorDetails and status 400 Bad Request when password is weak")
    void signup_ReturnsValidationErrorDetailsAndStatus400BadRequest_WhenPasswordIsWeak() {
        tryToSignupUserWithWeakPasswordValidateThatValidationErrorDetailsIsReturned("P4ss@"); // Less than 8 characters
        tryToSignupUserWithWeakPasswordValidateThatValidationErrorDetailsIsReturned("Password@"); // No number
        tryToSignupUserWithWeakPasswordValidateThatValidationErrorDetailsIsReturned("P4ssword"); // No special character
        tryToSignupUserWithWeakPasswordValidateThatValidationErrorDetailsIsReturned("p4ssword@"); // No uppercase letter
        tryToSignupUserWithWeakPasswordValidateThatValidationErrorDetailsIsReturned("P4SSWORD@"); // No lowercase letter
    }

    private void tryToSignupUserWithWeakPasswordValidateThatValidationErrorDetailsIsReturned(String password) {
        UserCreateRequest userCreateRequest = UserCreateRequestCreator.createUserCreateRequestWithPassword(password);

        ResponseEntity<ValidationErrorDetails> responseEntity = restTemplate.postForEntity("/signup",
                userCreateRequest, ValidationErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ValidationErrorDetails.class);

        assertThat(responseEntity.getBody().getFields()).isEqualTo("password");

        log.info(String.format("Fields messages: %s", responseEntity.getBody().getFieldsMessages()));
    }

    @Test
    @DisplayName("signup returns error details with constraint violation exception and status 400 Bad Request when " +
            "email already exists in the database")
    void signup_ReturnsErrorDetailsWithConstraintViolationExceptionAndStatus400BadRequest_WhenEmailAlreadyExistsInTheDatabase() {
        userRepository.save(UserCreator.createValidUser());
        UserCreateRequest userCreateRequest = UserCreateRequestCreator.createValidUserCreateRequest();

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.postForEntity("/signup",
                userCreateRequest, ErrorDetails.class);

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
    @DisplayName("signup returns error details with JSON parse error and status 400 Bad Request when request body " +
            "is an invalid JSON")
    void signup_ReturnsErrorDetailsWithJSONParseErrorAndStatus400BadRequest_WhenRequestBodyIsAnInvalidJSON() {
        String invalidJSON = "{ \"name\": \"news\"' }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(invalidJSON, headers);

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.exchange("/signup", HttpMethod.POST,
                httpEntity, ErrorDetails.class, 1L);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getTitle()).contains("JSON parse error");
    }

    @Test
    @DisplayName("login returns jwt token and status 200 Ok when successful")
    void login_ReturnsJwtTokenAndStatus200Ok_WhenSuccessful() {
        userRepository.save(UserCreator.createValidUser());

        LoginRequest loginRequest = LoginRequestCreator.createValidLoginRequest();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/login", loginRequest, String.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .matches(TestRegexPatterns.VALID_JWT_PATTERN);

        log.info(responseEntity.getBody());
    }

    @Test
    @DisplayName("login returns ErrorDetails and status 401 Unauthorized when credentials are invalid")
    void login_ReturnsErrorDetailsAndStatus401Unauthorized_WhenCredentialsAreInvalid() {
        userRepository.save(UserCreator.createValidUser());

        LoginRequest loginRequest = LoginRequestCreator.createValidLoginRequest();
        loginRequest.setEmail("some@email.com");

        ResponseEntity<ErrorDetails> responseEntity = restTemplate.postForEntity("/login",
                loginRequest, ErrorDetails.class);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .isInstanceOf(ErrorDetails.class);

        assertThat(responseEntity.getBody().getPath()).isEqualTo("/login");

        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Authentication failed: Incorrect email or password. Please try again.");
    }
}
