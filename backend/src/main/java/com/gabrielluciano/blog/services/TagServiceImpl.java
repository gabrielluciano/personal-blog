package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.dto.tag.TagCreateRequest;
import com.gabrielluciano.blog.dto.tag.TagResponse;
import com.gabrielluciano.blog.dto.tag.TagUpdateRequest;
import com.gabrielluciano.blog.exceptions.ConstraintViolationException;
import com.gabrielluciano.blog.exceptions.ResourceNotFoundException;
import com.gabrielluciano.blog.mappers.TagMapper;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

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
    public TagResponse findById(long id) {
        Tag tag = findByIdOrThrowResourceNotFoundException(id);
        return TagMapper.INSTANCE.tagToTagResponse(tag);
    }

    @Override
    public TagResponse save(TagCreateRequest tagCreateRequest) {
        throwConstraintViolationExceptionIfNameOrSlugAlreadyExists(tagCreateRequest.getName(), tagCreateRequest.getSlug());
        Tag tag = TagMapper.INSTANCE.tagCreateRequestToTag(tagCreateRequest);
        return TagMapper.INSTANCE.tagToTagResponse(tagRepository.save(tag));
    }

    @Override
    public void update(TagUpdateRequest tagUpdateRequest, long id) {
        Tag tag = findByIdOrThrowResourceNotFoundException(id);
        throwConstraintViolationExceptionIfNameOrSlugAlreadyExists(tagUpdateRequest.getName(),
                tagUpdateRequest.getSlug(), id);
        TagMapper.INSTANCE.updateTagFromTagUpdateRequest(tagUpdateRequest, tag);
        tagRepository.save(tag);
    }

    @Override
    public void deleteById(long id) {
        findByIdOrThrowResourceNotFoundException(id);
        tagRepository.deleteById(id);
    }

    private Tag findByIdOrThrowResourceNotFoundException(long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Tag.class, id));
    }

    private void throwConstraintViolationExceptionIfNameOrSlugAlreadyExists(String name, String slug) {
        tagRepository.findFirstByNameIgnoreCaseOrSlugIgnoreCase(name, slug)
                .ifPresent(tag -> prepareConstraintViolationExceptionAndThrow(tag, name, slug));
    }

    private void throwConstraintViolationExceptionIfNameOrSlugAlreadyExists(String name, String slug, long idToUpdate) {
        tagRepository.findFirstByNameIgnoreCaseOrSlugIgnoreCase(name, slug)
                .ifPresent(tag -> {
                    if (tag.getId().equals(idToUpdate)) return;
                    prepareConstraintViolationExceptionAndThrow(tag, name, slug);
                });
    }

    private void prepareConstraintViolationExceptionAndThrow(Tag savedTag, String name, String slug) {
        boolean isNameViolation = savedTag.getName().equalsIgnoreCase(name);
        boolean isSlugViolation = savedTag.getSlug().equalsIgnoreCase(slug);
        Map<String, String> violations = new LinkedHashMap<>();

        if (isNameViolation) violations.put("name", name);
        if (isSlugViolation) violations.put("slug", slug);

        throw new ConstraintViolationException("tags", violations);
    }
}
