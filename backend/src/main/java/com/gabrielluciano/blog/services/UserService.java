package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;

public interface UserService {

    UserResponse signup(UserCreateRequest userCreateRequest);
}
