package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.User;

import java.util.Set;

public class UserCreator {

    private static final User USER = User.builder()
            .id(1L)
            .name("John")
            .email("john@mail.com")
            .password("{bcrypt}$2a$10$22TVLWAtcnOpYwU3gXYNL.2ipe1jgiPLeM/AqWitvFTI37gc.yIBW") // pw: P@ssword1
            .build();

//    private static final User EDITOR_USER = User.builder()
//            .id(1L)
//            .name("Mike")
//            .email("mike@mail.com")
//            .password("{bcrypt}$2a$10$22TVLWAtcnOpYwU3gXYNL.2ipe1jgiPLeM/AqWitvFTI37gc.yIBW") // pw: P@ssword1
//            .roles(Set.of(Role.USER, Role.EDITOR))
//            .build();

    public static User createValidUser() {
        return User.builder()
                .id(USER.getId())
                .name(USER.getName())
                .email(USER.getEmail())
                .password(USER.getPassword())
                .roles(Set.of(Role.USER))
                .build();
    }

    public static User createValidEditorUser() {
        return User.builder()
                .id(USER.getId())
                .name(USER.getName())
                .email(USER.getEmail())
                .password(USER.getPassword())
                .roles(Set.of(Role.USER, Role.EDITOR))
                .build();
    }

    public static User createValidEditorUserToBeSaved() {
        User user = createValidEditorUser();
        user.setId(null);
        return user;
    }
}
