package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.repositories.UserRepository;
import com.gabrielluciano.blog.util.UserCreateRequestCreator;
import com.gabrielluciano.blog.util.UserCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        BDDMockito.when(userRepository.save(ArgumentMatchers.any()))
                .thenReturn(UserCreator.createValidUser());

        BDDMockito.when(passwordEncoder.encode(ArgumentMatchers.anyString()))
                .thenReturn(UserCreator.createValidUser().getPassword());
    }

    @Test
    @DisplayName("signup returns UserResponse when successful")
    void signup_ReturnsUserResponse_WhenSuccessful() {
        UserCreateRequest userCreateRequest = UserCreateRequestCreator.createValidUserCreateRequest();

        UserResponse userResponse = userService.signup(userCreateRequest);

        assertThat(userResponse).isNotNull();

        assertThat(userResponse.getName()).isEqualTo(userCreateRequest.getName());

        assertThat(userResponse.getEmail()).isEqualTo(userCreateRequest.getEmail());

        assertThat(userResponse.getRoles()).contains(Role.ADMIN);
    }
}
