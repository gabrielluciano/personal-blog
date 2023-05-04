package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.mappers.UserMapper;
import com.gabrielluciano.blog.models.User;
import com.gabrielluciano.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse signup(UserCreateRequest userCreateRequest) {
        userCreateRequest.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        User user = userRepository.save(UserMapper.INSTANCE.userCreateRequestToUser(userCreateRequest));
        return UserMapper.INSTANCE.userToUserResponse(user);
    }
}
