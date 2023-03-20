package com.gabrielluciano.blog.dto;

import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    private String name;
    private String email;
    private String password;

    public User toNewUser() {
        User user = new User(name, email, password);
        user.getRoles().add(Role.USER);
        return user;
    }

}
