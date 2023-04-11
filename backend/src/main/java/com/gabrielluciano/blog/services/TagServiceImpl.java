package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.mappers.TagMapper;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public Page<TagResponse> list(Pageable pageable) {
        return tagRepository.findAll(pageable)
                .map(TagMapper.INSTANCE::tagToTagResponse);
    }

    @Override
    public TagResponse findByIdOrThrowResourceNotFoundException(long id) {
        Tag tag = findTagByIdOrThrowResourceNotFoundException(id);
        return TagMapper.INSTANCE.tagToTagResponse(tag);
    }

    @Override
    public TagResponse save(TagCreateRequest tagCreateRequest) {
        Tag tag = TagMapper.INSTANCE.tagCreateRequestToTag(tagCreateRequest);
        return TagMapper.INSTANCE.tagToTagResponse(tagRepository.save(tag));
    }

    @Override
    public void update(TagUpdateRequest tagUpdateRequest, long id) {
        Tag tag = findTagByIdOrThrowResourceNotFoundException(id);
        TagMapper.INSTANCE.updateTagFromTagUpdateRequest(tagUpdateRequest, tag);
        tagRepository.save(tag);
    }

    @Override
    public void deleteById(long id) {
        findTagByIdOrThrowResourceNotFoundException(id);
        tagRepository.deleteById(id);
    }

    private Tag findTagByIdOrThrowResourceNotFoundException(long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Tag.class, id));
    }
}
