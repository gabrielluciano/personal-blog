package com.gabrielluciano.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    private String email;
    private String password;

    public String getEmail() {
        return email == null ? "" : email;
    }
}
