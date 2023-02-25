package com.gabrielluciano.blog.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.entities.User;

import java.time.Instant;
import java.util.stream.Collectors;

public class JwtUtil {

    private final long iat;
    private final long exp;
    private final String secret;

    public JwtUtil(String secret) {
        this.secret = secret;
        iat = Instant.now().getEpochSecond();
        exp = iat + 60 * 60 * 24 * 2;
    }

    public String createToken(User user) throws JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("iat", iat)
                .withClaim("exp", exp)
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("roles", user.getRoles().stream()
                        .map(Role::name)
                        .collect(Collectors.toList()))
                .sign(algorithm);
    }

    public JwtPayload getPayload(User user) {
        return new JwtPayload(user.getId(), user.getEmail(), user.getRoles(), iat, exp);
    }
}
