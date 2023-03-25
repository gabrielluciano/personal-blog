package com.gabrielluciano.blog.dto.user;

import com.gabrielluciano.blog.security.jwt.JwtPayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponse {

    private String token;

    private JwtPayload payload;

}
