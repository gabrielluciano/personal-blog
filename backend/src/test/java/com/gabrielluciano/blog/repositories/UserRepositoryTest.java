package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.User;
import com.gabrielluciano.blog.util.UserCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByEmailIgnoreCase returns optional of user when user is found")
    void findByEmailIgnoreCase_ReturnsOptionalOfUser_WhenUserIsFound() {
        User savedUser = userRepository.save(UserCreator.createValidUser());

        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(savedUser.getEmail());

        assertThat(userOptional)
                .isNotNull()
                .isPresent()
                .contains(savedUser);
    }

    @Test
    @DisplayName("findByEmailIgnoreCase returns optional empty when user is not found")
    void findByEmailIgnoreCase_ReturnsOptionalEmpty_WhenUserIsNotFound() {
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase("john@email.com");

        assertThat(userOptional)
                .isNotNull()
                .isEmpty();
    }
}
