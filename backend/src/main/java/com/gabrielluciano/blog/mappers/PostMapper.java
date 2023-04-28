package com.gabrielluciano.blog.mappers;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.models.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostResponse postToPostResponse(Post post);

    @Mapping(target = "published", constant = "false")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now(java.time.ZoneOffset.UTC))")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now(java.time.ZoneOffset.UTC))")
    @Mapping(target = "tags", expression = "java(java.util.Collections.emptySet())")
    Post postCreateRequestToPost(PostCreateRequest postCreateRequest);

    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now(java.time.ZoneOffset.UTC))")
    void updatePostFromPostUpdateRequest(PostUpdateRequest postUpdateRequest, @MappingTarget Post post);
}
