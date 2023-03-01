package com.gabrielluciano.blog.security.managers;

import com.gabrielluciano.blog.security.providers.JwtAuthProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppAuthenticationManager implements AuthenticationManager {

    private final JwtAuthProvider provider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (provider.supports(authentication.getClass())) {
            return provider.authenticate(authentication);
        }

        return null;
    }
}
