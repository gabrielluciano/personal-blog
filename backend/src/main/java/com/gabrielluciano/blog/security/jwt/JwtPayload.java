package com.gabrielluciano.blog.security.jwt;

import com.gabrielluciano.blog.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class JwtPayload {

    private Long id;
    private String email;
    private Set<Role> roles;

}
