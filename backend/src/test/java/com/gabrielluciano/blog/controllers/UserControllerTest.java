package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.user.LoginRequest;
import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.exceptions.InvalidCredentialsException;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.User;
import com.gabrielluciano.blog.services.UserService;
import com.gabrielluciano.blog.util.LoginRequestCreator;
import com.gabrielluciano.blog.util.TestRegexPatterns;
import com.gabrielluciano.blog.util.UserCreateRequestCreator;
import com.gabrielluciano.blog.util.UserResponseCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
@Log4j2
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        BDDMockito.when(userService.signup(ArgumentMatchers.any(UserCreateRequest.class)))
                .thenReturn(UserResponseCreator.createValidUserResponse());

        BDDMockito.when(userService.login(ArgumentMatchers.any(LoginRequest.class)))
                .thenReturn(JWT_TOKEN);
    }

    @Test
    @DisplayName("signup returns UserResponse and status 201 Created when successful")
    void signup_ReturnsUserResponseAndStatus201Created_WhenSuccessful() {
        UserCreateRequest userCreateRequest = UserCreateRequestCreator.createValidUserCreateRequest();

        ResponseEntity<UserResponse> responseEntity = userController.signup(userCreateRequest);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(responseEntity.getBody()).isNotNull();

        assertThat(responseEntity.getBody().getName()).isEqualTo(userCreateRequest.getName());

        assertThat(responseEntity.getBody().getEmail()).isEqualTo(userCreateRequest.getEmail());

        assertThat(responseEntity.getBody().getRoles()).contains(Role.ADMIN);
    }

    @Test
    @DisplayName("login returns jwt token and status 200 Ok when successful")
    void login_ReturnsJwtTokenAndStatus200Ok_WhenSuccessful() {
        LoginRequest loginRequest = LoginRequestCreator.createValidLoginRequest();

        ResponseEntity<String> responseEntity = userController.login(loginRequest);

        assertThat(responseEntity).isNotNull();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .matches(TestRegexPatterns.VALID_JWT_PATTERN);

        log.info(responseEntity.getBody());
    }

    @Test
    @DisplayName("login throws InvalidCredentialsException when User is not found or password is incorrect")
    void login_InvalidCredentialsException_WhenUserOrPasswordIsIncorrectIsNotFound() {
        LoginRequest loginRequest = LoginRequestCreator.createValidLoginRequest();

        BDDMockito.when(userService.login(ArgumentMatchers.any(LoginRequest.class)))
                .thenThrow(new InvalidCredentialsException());

        assertThatExceptionOfType(InvalidCredentialsException.class)
                .isThrownBy(() -> userController.login(loginRequest))
                .withMessageContaining("Authentication failed: Incorrect email or password. Please try again.");
    }
}
