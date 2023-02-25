package com.gabrielluciano.blog.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.entities.User;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Collectors;

public class JwtUtil {

    private JwtUtil() {
    }

    public static String createToken(User user, String secret) throws JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("roles", user.getRoles().stream()
                        .map(Role::name)
                        .collect(Collectors.toList()))
                .sign(algorithm);
    }
}
