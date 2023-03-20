package com.gabrielluciano.blog.security.models;

import com.gabrielluciano.blog.models.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class SecurityRole implements GrantedAuthority {

    private final Role role;

    @Override
    public String getAuthority() {
        return "ROLE_" + role.name();
    }
}
