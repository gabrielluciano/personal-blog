package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.models.User;

public class UserCreateRequestCreator {

    private static final User user = UserCreator.createValidUser();

    public static UserCreateRequest createValidUserCreateRequest() {
        return createUserCreateRequestWithNameAndEmail(user.getName(), user.getEmail());
    }

    public static UserCreateRequest createUserCreateRequestWithName(String name) {
        return createUserCreateRequestWithNameAndEmail(name, user.getEmail());
    }

    public static UserCreateRequest createUserCreateRequestWithEmail(String email) {
        return createUserCreateRequestWithNameAndEmail(user.getName(), email);
    }

    public static UserCreateRequest createUserCreateRequestWithPassword(String password) {
        return UserCreateRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(password)
                .build();
    }

    public static UserCreateRequest createUserCreateRequestWithNameAndEmail(String name, String email) {

        return UserCreateRequest.builder()
                .name(name)
                .email(email)
                .password("P@ssword1")
                .build();
    }
}
