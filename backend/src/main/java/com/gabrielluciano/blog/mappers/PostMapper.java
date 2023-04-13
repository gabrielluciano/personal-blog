package com.gabrielluciano.blog.mappers;

import com.gabrielluciano.blog.dto.post.PostResponse;
import com.gabrielluciano.blog.models.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostResponse postToPostResponse(Post post);
}
