package com.gabrielluciano.blog.mappers;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.models.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag toTag(TagCreateRequest tagCreateRequest);

    void updateTagFromTagUpdateRequest(TagUpdateRequest tagUpdateRequest, @MappingTarget Tag tag);

    TagResponse toTagResponse(Tag tag);
}
