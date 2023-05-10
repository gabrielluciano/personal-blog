package com.gabrielluciano.blog.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JWTUtil {

    private final long tokenDurationInDays;

    public JWTUtil() {
        tokenDurationInDays = 1L;
    }

    @Value("${api.secret}")
    private String API_SECRET;

    public String createToken(User user) {

        Algorithm algorithm = Algorithm.HMAC256(API_SECRET);

        return JWT.create()
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(Duration.ofDays(tokenDurationInDays)))
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("roles", user.getRoles().stream()
                        .map(Role::name)
                        .toList())
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(API_SECRET);

        JWTVerifier verifier = JWT.require(algorithm)
                .acceptExpiresAt(getTokenDurationInSeconds())
                .build();

        return verifier.verify(token);
    }

    private long getTokenDurationInSeconds() {
        return tokenDurationInDays * 24 * 60 * 60;
    }
}
