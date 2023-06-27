package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.user.LoginRequest;
import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("signup")
    public ResponseEntity<UserResponse> signup(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        return new ResponseEntity<>(userService.signup(userCreateRequest), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>(userService.login(loginRequest), headers, HttpStatus.OK);
    }

    @PutMapping("users/{id}/roles/editor")
    public ResponseEntity<Void> addEditorRole(@PathVariable long id) {
        userService.addEditorRole(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("users/{id}/roles/editor")
    public ResponseEntity<Void> removeEditorRole(@PathVariable long id) {
        userService.removeEditorRole(id);
        return ResponseEntity.noContent().build();
    }
}
