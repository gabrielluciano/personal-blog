package com.gabrielluciano.blog.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class JWTUtil {

    private static final long TOKEN_DURATION_IN_DAYS = 1;

    @Value("${api.secret}")
    private String API_SECRET;

    public String createToken(User user) {

        Algorithm algorithm = Algorithm.HMAC256(API_SECRET);

        return JWT.create()
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(Duration.ofDays(TOKEN_DURATION_IN_DAYS)))
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("roles", user.getRoles().stream()
                        .map(Role::name)
                        .toList())
                .sign(algorithm);
    }
}
