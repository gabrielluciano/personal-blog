package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.User;

import java.util.Set;

public class UserCreator {

    private static final User user = User.builder()
            .id(1L)
            .name("John")
            .email("john@mail.com")
            .password("{bcrypt}$2a$10$QFbx9CplU/dr7.mxlmkuK.3Pkeih7jfUT/9Nt7Hcw39dpFWYOZQje") // pw: 12345
            .roles(Set.of(Role.ADMIN))
            .build();

    public static User createValidUser() {
        return user;
    }
}
