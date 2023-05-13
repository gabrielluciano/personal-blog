package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.user.LoginRequest;
import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;

public interface UserService {

    UserResponse signup(UserCreateRequest userCreateRequest);

    String login(LoginRequest loginRequest);

    void addEditorRole(long id);

    void removeEditorRole(long id);
}
