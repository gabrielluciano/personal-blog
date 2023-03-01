package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.LoginRequestDTO;
import com.gabrielluciano.blog.dto.LoginResponseDTO;
import com.gabrielluciano.blog.exceptions.InvalidEmailOrPasswordException;
import com.gabrielluciano.blog.models.entities.User;
import com.gabrielluciano.blog.repositories.UserRepository;
import com.gabrielluciano.blog.security.jwt.JwtPayload;
import com.gabrielluciano.blog.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {

        String email = loginRequestDTO.getEmail();
        Optional<User> optionalUser = repository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new InvalidEmailOrPasswordException();
        }

        User user = optionalUser.get();
        boolean passwordMatches = passwordEncoder
                .matches(loginRequestDTO.getPassword(), user.getPassword());

        if (passwordMatches) {
            JwtPayload payload = jwtUtil.getPayload(user);
            String token = jwtUtil.createToken(user);
            return new LoginResponseDTO(token, payload);
        }

        throw new InvalidEmailOrPasswordException();
    }
}
