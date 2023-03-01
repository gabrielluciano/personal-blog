package com.gabrielluciano.blog.security.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.gabrielluciano.blog.security.authentication.JwtAuthentication;
import com.gabrielluciano.blog.security.jwt.JwtUtil;
import com.gabrielluciano.blog.security.managers.AppAuthenticationManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AppAuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new BadCredentialsException("Invalid authorization header format");
            }

            final int TOKEN_BEGIN_INDEX = 7;
            String token = authHeader.substring(TOKEN_BEGIN_INDEX);
            DecodedJWT decodedJWT = jwtUtil.verifyToken(token);

            if (isTokenExpired(decodedJWT)) {
                throw new BadCredentialsException("Token is expired");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new JwtAuthentication(decodedJWT, false, null)
            );

            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // TODO log exception here
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenExpired(DecodedJWT decodedJWT) throws ParseException {
        return jwtUtil.isTokenExpired(decodedJWT);
    }
}
