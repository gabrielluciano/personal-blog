package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.user.LoginRequest;
import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    public ResponseEntity<UserResponse> signup(UserCreateRequest userCreateRequest) {
        return new ResponseEntity<>(userService.signup(userCreateRequest), HttpStatus.CREATED);
    }

    public ResponseEntity<String> login(LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }
}
