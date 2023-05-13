package com.gabrielluciano.blog.security.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.gabrielluciano.blog.security.jwt.JWTUtil;
import com.gabrielluciano.blog.security.services.AppUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
@Log4j2
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private AppUserDetailsService appUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeaderIsNotValid(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(authHeader);

        try {
            DecodedJWT decodedJWT = jwtUtil.verifyToken(token);
            String email = decodedJWT.getClaim("email").asString();

            UserDetails userDetails = appUserDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken
                    .authenticated(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private boolean authHeaderIsNotValid(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.split(" ").length != 2;
    }

    private String getToken(String authHeader) {
        return authHeader.split(" ")[1];
    }
}
