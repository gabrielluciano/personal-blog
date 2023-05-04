package com.gabrielluciano.blog.mappers;

import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.User;
import com.gabrielluciano.blog.util.UserCreateRequestCreator;
import com.gabrielluciano.blog.util.UserCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    @DisplayName("userCreateRequestToUser returns User with encrypted password when successful")
    void userCreateRequestToUser_ReturnsUserWithEncryptedPassword_WhenSuccessful() {
        UserCreateRequest userCreateRequest = UserCreateRequestCreator.createValidUserCreateRequest();

        User user = UserMapper.INSTANCE.userCreateRequestToUser(userCreateRequest);

        assertThat(user).isNotNull();

        assertThat(user.getName()).isEqualTo(userCreateRequest.getName());

        assertThat(user.getEmail()).isEqualTo(userCreateRequest.getEmail());

        assertThat(user.getPassword()).isEqualTo(userCreateRequest.getPassword());

        assertThat(user.getId()).isNull();

        assertThat(user.getRoles())
                .hasSize(1)
                .contains(Role.ADMIN);
    }

    @Test
    @DisplayName("userToUserResponse returns UserResponse when successful")
    void userToUserResponse_ReturnsUserResponse_WhenSuccessful() {
        User user = UserCreator.createValidUser();

        UserResponse userResponse = UserMapper.INSTANCE.userToUserResponse(user);

        assertThat(userResponse).isNotNull();

        assertThat(userResponse.getId()).isEqualTo(user.getId());

        assertThat(userResponse.getName()).isEqualTo(user.getName());

        assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());

        assertThat(userResponse.getRoles()).isEqualTo(user.getRoles());
    }
}
