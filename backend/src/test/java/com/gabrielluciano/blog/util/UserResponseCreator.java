package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.models.User;

public class UserResponseCreator {

    private static final User user = UserCreator.createValidUser();
    private static final User adminuser = UserCreator.createValidAdminUser();

    public static UserResponse createValidUserResponse() {

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    public static UserResponse createValidAdminUserResponse() {

        return UserResponse.builder()
                .id(adminuser.getId())
                .name(adminuser.getName())
                .email(adminuser.getEmail())
                .roles(adminuser.getRoles())
                .build();
    }
}
