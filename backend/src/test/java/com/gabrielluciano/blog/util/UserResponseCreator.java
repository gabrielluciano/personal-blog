package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.models.User;

public class UserResponseCreator {

    private static final User USER = UserCreator.createValidUser();
    private static final User EDITOR_USER = UserCreator.createValidEditorUser();

    public static UserResponse createValidUserResponse() {

        return UserResponse.builder()
                .id(USER.getId())
                .name(USER.getName())
                .email(USER.getEmail())
                .roles(USER.getRoles())
                .build();
    }

    public static UserResponse createValidEditorUserResponse() {

        return UserResponse.builder()
                .id(EDITOR_USER.getId())
                .name(EDITOR_USER.getName())
                .email(EDITOR_USER.getEmail())
                .roles(EDITOR_USER.getRoles())
                .build();
    }
}
