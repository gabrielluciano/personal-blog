package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.models.User;

public class UserResponseCreator {

    private static final User user = UserCreator.createValidUser();

    public static UserResponse createValidUserResponse() {

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
