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
        return getHttpHeadersForUserWithIdEmailAndRoles(1L, "john@mail.com", Set.of(Role.ADMIN));
    }

    public HttpHeaders getHttpHeadersForUserWithNoRole() {
        return getHttpHeadersForUserWithIdEmailAndRoles(2L, "mark@mail.com", Set.of());
    }

    private HttpHeaders getHttpHeadersForUserWithIdEmailAndRoles(long id, String email, Set<Role> roles) {
        User user = User.builder()
                .id(id)
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
