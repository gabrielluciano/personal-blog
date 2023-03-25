package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.error.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.mappers.TagMapper;
import com.gabrielluciano.blog.models.entities.Tag;
import com.gabrielluciano.blog.repositories.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Optional<Tag> findOptionalById(Long id) {
        return tagRepository.findById(id);
    }

    public Tag findByIdOrThrowException(Long id) {
        return findOptionalById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Tag.class, id));
    }

    public Page<Tag> list(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    public Tag create(TagCreateRequest tagCreateRequest) {
        Tag tag = TagMapper.INSTANCE.toTag(tagCreateRequest);
        return tagRepository.save(tag);
    }

    public void update(TagUpdateRequest tagUpdateRequest) {
        Tag tag = findByIdOrThrowException(tagUpdateRequest.getId());
        TagMapper.INSTANCE.updateTagFromTagUpdateRequest(tagUpdateRequest, tag);
    }

    public void deleteById(Long id) {
        findByIdOrThrowException(id);
        tagRepository.deleteById(id);
    }
}
