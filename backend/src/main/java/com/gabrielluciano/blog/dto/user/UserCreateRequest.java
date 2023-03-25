package com.gabrielluciano.blog.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {

    private String name;
    private String email;
    private String password;
}
