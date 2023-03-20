package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.LoginRequest;
import com.gabrielluciano.blog.dto.LoginResponse;
import com.gabrielluciano.blog.dto.SignupRequest;
import com.gabrielluciano.blog.models.entities.User;
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

    private final UserService service;

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> signin(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(service.authenticate(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequest signupRequest) {
        return new ResponseEntity<>(service.createUser(signupRequest), HttpStatus.CREATED);
    }

}
