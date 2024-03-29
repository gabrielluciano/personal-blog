package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.dto.user.LoginRequest;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.User;
import com.gabrielluciano.blog.repositories.UserRepository;
import com.gabrielluciano.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final UserRepository userRepository;
    private final UserService userService;

    public HttpHeaders getHttpHeadersForAdminUser() {
        return getHttpHeadersForUserWithIdEmailAndRoles("adminuser@mail.com", Set.of(Role.USER, Role.EDITOR, Role.ADMIN));
    }

    public HttpHeaders getHttpHeadersForEditorUser() {
        return getHttpHeadersForUserWithIdEmailAndRoles("editoruser@mail.com", Set.of(Role.USER, Role.EDITOR));
    }

    public HttpHeaders getHttpHeadersForUser() {
        return getHttpHeadersForUserWithIdEmailAndRoles("user@mail.com", Set.of(Role.USER));
    }

    private HttpHeaders getHttpHeadersForUserWithIdEmailAndRoles(String email, Set<Role> roles) {
        User user = User.builder()
                .name(email)
                .email(email)
                .password("{bcrypt}$2a$10$22TVLWAtcnOpYwU3gXYNL.2ipe1jgiPLeM/AqWitvFTI37gc.yIBW") // pw: P@ssword1
                .roles(roles)
                .build();

        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password("P@ssword1")
                .build();

        userRepository.save(user);
        String token = userService.login(loginRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }
}
