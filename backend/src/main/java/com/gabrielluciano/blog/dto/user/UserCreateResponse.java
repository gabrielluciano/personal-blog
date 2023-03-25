package com.gabrielluciano.blog.dto.user;

import com.gabrielluciano.blog.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserCreateResponse {

    private Long id;
    private String name;
    private String email;
    private Set<Role> roles;
}
