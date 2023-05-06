package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.user.LoginRequest;
import com.gabrielluciano.blog.dto.user.UserCreateRequest;

public class LoginRequestCreator {

    private static final UserCreateRequest userCreateRequest = UserCreateRequestCreator.createValidUserCreateRequest();

    private LoginRequestCreator() {
    }

    public static LoginRequest createValidLoginRequest() {
        return LoginRequest.builder()
                .email(userCreateRequest.getEmail())
                .password(userCreateRequest.getPassword())
                .build();
    }

}
