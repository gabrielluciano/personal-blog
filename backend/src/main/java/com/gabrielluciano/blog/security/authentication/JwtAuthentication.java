package com.gabrielluciano.blog.security.authentication;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.gabrielluciano.blog.models.entities.User;
import com.gabrielluciano.blog.security.models.SecurityUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
public class JwtAuthentication implements Authentication {

    @Getter
    private final DecodedJWT decodedJWT;
    private boolean authenticated;
    private SecurityUser userDetails;

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    @Override
    public String getName() {
        return null;
    }
}
