package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserLoginRequest;
import com.gabrielluciano.blog.dto.user.UserLoginResponse;
import com.gabrielluciano.blog.error.exceptions.InvalidCredentialsException;
import com.gabrielluciano.blog.mappers.UserMapper;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.entities.User;
import com.gabrielluciano.blog.repositories.UserRepository;
import com.gabrielluciano.blog.security.jwt.JwtPayload;
import com.gabrielluciano.blog.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {

        String email = userLoginRequest.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        throwInvalidCredentialsExceptionIfPasswordsDoNotMatch(userLoginRequest.getPassword(), user.getPassword());

        JwtPayload payload = jwtUtil.getPayload(user);
        String token = jwtUtil.createToken(user);
        return new UserLoginResponse(token, payload);
    }

    public User create(UserCreateRequest userCreateRequest) {
        User user = UserMapper.INSTANCE.toUser(userCreateRequest);
        user.getRoles().add(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    private void throwInvalidCredentialsExceptionIfPasswordsDoNotMatch(String actual, String expected) {
        boolean match = passwordEncoder.matches(actual, expected);
        if (!match) {
            throw new InvalidCredentialsException();
        }
    }
}
