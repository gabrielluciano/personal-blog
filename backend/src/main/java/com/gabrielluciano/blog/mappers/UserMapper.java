package com.gabrielluciano.blog.mappers;

import com.gabrielluciano.blog.dto.user.UserCreateRequest;
import com.gabrielluciano.blog.dto.user.UserResponse;
import com.gabrielluciano.blog.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roles", expression = "java(java.util.Set.of(com.gabrielluciano.blog.models.Role.USER))")
    User userCreateRequestToUser(UserCreateRequest userCreateRequest);

    UserResponse userToUserResponse(User user);
}
