package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.services.UserService;
import com.gabrielluciano.blog.util.UserCreateRequestCreator;
import com.gabrielluciano.blog.util.UserResponseCreator;
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

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        BDDMockito.when(userService.signup(ArgumentMatchers.any(UserCreateRequest.class)))
                .thenReturn(UserResponseCreator.createValidUserResponse());
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
}
