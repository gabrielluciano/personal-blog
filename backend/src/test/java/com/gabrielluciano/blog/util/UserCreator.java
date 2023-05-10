package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.User;

import java.util.Set;

public class UserCreator {

    private static final User user = User.builder()
            .id(1L)
            .name("John")
            .email("john@mail.com")
            .password("{bcrypt}$2a$10$22TVLWAtcnOpYwU3gXYNL.2ipe1jgiPLeM/AqWitvFTI37gc.yIBW") // pw: P@ssword1
            .roles(Set.of(Role.ADMIN))
            .build();

    public static User createValidUser() {
        return user;
    }
}
