package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.user.LoginRequest;
import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.exceptions.ConstraintViolationException;
import com.gabrielluciano.blog.exceptions.InvalidCredentialsException;
import com.gabrielluciano.blog.mappers.UserMapper;
import com.gabrielluciano.blog.models.User;
import com.gabrielluciano.blog.repositories.UserRepository;
import com.gabrielluciano.blog.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Override
    public UserResponse signup(UserCreateRequest userCreateRequest) {
        throwConstraintViolationExceptionIfEmailAlreadyExist(userCreateRequest.getEmail());
        userCreateRequest.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        User user = userRepository.save(UserMapper.INSTANCE.userCreateRequestToUser(userCreateRequest));
        return UserMapper.INSTANCE.userToUserResponse(user);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        User user = findUserByEmailOrThrowInvalidCredentialsException(loginRequest.getEmail());
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return jwtUtil.createToken(user);
        }
        throw new InvalidCredentialsException();
    }

    private User findUserByEmailOrThrowInvalidCredentialsException(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(InvalidCredentialsException::new);
    }

    private void throwConstraintViolationExceptionIfEmailAlreadyExist(String email) {
        userRepository.findByEmailIgnoreCase(email)
                .ifPresent(user -> prepareConstraintViolationExceptionAndThrow(email));
    }

    private void prepareConstraintViolationExceptionAndThrow(String email) {
        throw new ConstraintViolationException("users", Map.of("email", email));
    }
}
