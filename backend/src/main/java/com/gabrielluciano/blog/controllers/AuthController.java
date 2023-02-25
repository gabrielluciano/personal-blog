package com.gabrielluciano.blog.controllers;

import com.gabrielluciano.blog.dto.LoginRequestDTO;
import com.gabrielluciano.blog.dto.LoginResponseDTO;
import com.gabrielluciano.blog.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/signin")
    public LoginResponseDTO signin(@RequestBody LoginRequestDTO loginRequestDTO) {
        return service.authenticate(loginRequestDTO);
    }

}
