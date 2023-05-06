package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.user.LoginRequest;
import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.exceptions.ConstraintViolationException;
import com.gabrielluciano.blog.exceptions.InvalidCredentialsException;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.User;
import com.gabrielluciano.blog.repositories.UserRepository;
import com.gabrielluciano.blog.security.jwt.JWTUtil;
import com.gabrielluciano.blog.util.LoginRequestCreator;
import com.gabrielluciano.blog.util.TestRegexPatterns;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @BeforeEach
    void setUp() {
        String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        BDDMockito.when(userRepository.save(ArgumentMatchers.any()))
                .thenReturn(UserCreator.createValidUser());

        BDDMockito.when(userRepository.findByEmailIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        BDDMockito.when(passwordEncoder.encode(ArgumentMatchers.anyString()))
                .thenReturn(UserCreator.createValidUser().getPassword());

        BDDMockito.when(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(true);

        BDDMockito.when(jwtUtil.createToken(ArgumentMatchers.any(User.class)))
                .thenReturn(JWT_TOKEN);
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

    @Test
    @DisplayName("signup throws ConstraintViolationException when email already exists in database")
    void signup_throwsConstraintViolationException_WhenEmailAlreadyExistsInDatabase() {
        UserCreateRequest userCreateRequest = UserCreateRequestCreator.createValidUserCreateRequest();

        BDDMockito.when(userRepository.findByEmailIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(UserCreator.createValidUser()));

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> userService.signup(userCreateRequest))
                .withMessageContaining("The following attributes already exist in one or more entries in the database table 'users' => email: "
                        + userCreateRequest.getEmail());
    }

    @Test
    @DisplayName("login returns jwt token when successful")
    void login_ReturnsJwtToken_WhenSuccessful() {
        LoginRequest loginRequest = LoginRequestCreator.createValidLoginRequest();

        BDDMockito.when(userRepository.findByEmailIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(UserCreator.createValidUser()));

        String token = userService.login(loginRequest);

        assertThat(token)
                .isNotNull()
                .matches(TestRegexPatterns.VALID_JWT_PATTERN);
    }

    @Test
    @DisplayName("login throws InvalidCredentialsException when User is not found")
    void login_InvalidCredentialsException_WhenUserIsNotFound() {
        LoginRequest loginRequest = LoginRequestCreator.createValidLoginRequest();

        BDDMockito.when(userRepository.findByEmailIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(InvalidCredentialsException.class)
                .isThrownBy(() -> userService.login(loginRequest))
                .withMessageContaining("Authentication failed: Incorrect email or password. Please try again.");
    }

    @Test
    @DisplayName("login throws InvalidCredentialsException when password do not match")
    void login_InvalidCredentialsException_WhenPasswordDoNotMatch() {
        LoginRequest loginRequest = LoginRequestCreator.createValidLoginRequest();

        BDDMockito.when(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(false);

        assertThatExceptionOfType(InvalidCredentialsException.class)
                .isThrownBy(() -> userService.login(loginRequest))
                .withMessageContaining("Authentication failed: Incorrect email or password. Please try again.");
    }
}
