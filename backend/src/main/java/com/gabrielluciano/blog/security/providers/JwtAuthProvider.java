package com.gabrielluciano.blog.security.providers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.gabrielluciano.blog.security.authentication.JwtAuthentication;
import com.gabrielluciano.blog.security.services.AppUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class JwtAuthProvider implements AuthenticationProvider {

    private final AppUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var jwtAuthentication = (JwtAuthentication) authentication;
        DecodedJWT token = jwtAuthentication.getDecodedJWT();
        UserDetails ud = userDetailsService.loadUserByUsername(token.getClaim("email").asString());

        return new JwtAuthentication(null, true, ud);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.equals(authentication);
    }
}
