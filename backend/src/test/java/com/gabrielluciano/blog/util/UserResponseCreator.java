package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.models.User;

public class UserResponseCreator {

    private static final User USER = UserCreator.createValidUser();

    public static UserResponse createValidUserResponse() {

        return UserResponse.builder()
                .id(USER.getId())
                .name(USER.getName())
                .email(USER.getEmail())
                .roles(USER.getRoles())
                .build();
    }
}
