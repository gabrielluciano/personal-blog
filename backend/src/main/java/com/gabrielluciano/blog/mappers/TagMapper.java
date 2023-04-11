package com.gabrielluciano.blog.mappers;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.models.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag tagCreateRequestToTag(TagCreateRequest tagCreateRequest);

    void updateTagFromTagUpdateRequest(TagUpdateRequest tagUpdateRequest, @MappingTarget Tag tag);

    TagResponse tagToTagResponse(Tag tag);
}
