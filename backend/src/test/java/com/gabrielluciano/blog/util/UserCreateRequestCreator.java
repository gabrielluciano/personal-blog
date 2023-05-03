package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.models.User;

public class UserCreateRequestCreator {

    private static final User user = UserCreator.createValidUser();

    public static UserCreateRequest createValidUserCreateRequest() {

        return UserCreateRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
