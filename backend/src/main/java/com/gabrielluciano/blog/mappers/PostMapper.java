package com.gabrielluciano.blog.mappers;

import com.gabrielluciano.blog.dto.post.PostCreateRequest;
import com.gabrielluciano.blog.dto.post.PostPagedResponse;
import com.gabrielluciano.blog.dto.post.PostSingleResponse;
import com.gabrielluciano.blog.dto.post.PostUpdateRequest;
import com.gabrielluciano.blog.models.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostSingleResponse toSinglePostResponse(Post post);

    PostPagedResponse toPagedPostResponse(Post post);

    Post toPost(PostCreateRequest postCreateRequest);

    void updatePostFromPostUpdateRequest(PostUpdateRequest postUpdateRequest, @MappingTarget Post post);
}
