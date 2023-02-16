package com.gabrielluciano.blog.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${auth.secret}")
    private String AUTH_SECRET;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(isInvalidJwtAuthHeader(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = parseJwtToken(authHeader);
        try {
            DecodedJWT decodedJWT = decodeJwt(token);
        } catch (JWTVerificationException ex) {
            // TODO Properly handle this exception
        }

    }

    private boolean isInvalidJwtAuthHeader(String authHeader) {
        return authHeader == null && !authHeader.contains("Bearer ");
    }

    private String parseJwtToken(String authHeader) {
        return authHeader.split(" ")[1];
    }

    private DecodedJWT decodeJwt(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(AUTH_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("personalblog").build();
        return verifier.verify(token);
    }
}
