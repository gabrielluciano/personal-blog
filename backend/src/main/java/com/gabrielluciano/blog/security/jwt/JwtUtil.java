package com.gabrielluciano.blog.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${api.secret}")
    private String secret;
    private final long iat;
    private final long exp;

    public JwtUtil() {
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

    public DecodedJWT verifyToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }

    public JwtPayload getPayload(User user) {
        return new JwtPayload(user.getId(), user.getEmail(), user.getRoles(), iat, exp);
    }

    public boolean isTokenExpired(DecodedJWT decodedJWT) throws ParseException {
        return decodedJWT.getExpiresAt()
                .before(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
    }
}
