package com.gabrielluciano.blog.dto;

import com.gabrielluciano.blog.security.jwt.JwtPayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;

    private JwtPayload payload;

}
