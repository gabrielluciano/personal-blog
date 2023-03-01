package com.gabrielluciano.blog.security;

import com.gabrielluciano.blog.models.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class SecurityRole implements GrantedAuthority {

    private final Role role;
    @Override
    public String getAuthority() {
        return role.name();
    }
}
