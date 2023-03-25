package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserCreateResponse;
import com.gabrielluciano.blog.dto.user.UserLoginRequest;
import com.gabrielluciano.blog.dto.user.UserLoginResponse;
import com.gabrielluciano.blog.mappers.UserMapper;
import com.gabrielluciano.blog.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> signin(@RequestBody UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok(userService.login(userLoginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserCreateResponse> signup(@RequestBody UserCreateRequest userCreateRequest) {
        UserCreateResponse userCreateResponse = UserMapper.INSTANCE
                .toUserCreateResponse(userService.create(userCreateRequest));
        return new ResponseEntity<>(userCreateResponse, HttpStatus.CREATED);
    }

}
