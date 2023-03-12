package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.LoginRequest;
import com.gabrielluciano.blog.dto.LoginResponse;
import com.gabrielluciano.blog.error.exceptions.InvalidCredentialsException;
import com.gabrielluciano.blog.models.entities.User;
import com.gabrielluciano.blog.repositories.UserRepository;
import com.gabrielluciano.blog.security.jwt.JwtPayload;
import com.gabrielluciano.blog.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse authenticate(LoginRequest loginRequest) {

        String email = loginRequest.getEmail();
        Optional<User> optionalUser = repository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new InvalidCredentialsException();
        }

        User user = optionalUser.get();
        boolean passwordMatches = passwordEncoder
                .matches(loginRequest.getPassword(), user.getPassword());

        if (passwordMatches) {
            JwtPayload payload = jwtUtil.getPayload(user);
            String token = jwtUtil.createToken(user);
            return new LoginResponse(token, payload);
        }

        throw new InvalidCredentialsException();
    }
}
