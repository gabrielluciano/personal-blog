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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
        throwConstraintViolationExceptionIfNameOrSlugAlreadyExists(tagCreateRequest.getName(), tagCreateRequest.getSlug());
        Tag tag = TagMapper.INSTANCE.tagCreateRequestToTag(tagCreateRequest);
        return TagMapper.INSTANCE.tagToTagResponse(tagRepository.save(tag));
    }

    @Override
    public void update(TagUpdateRequest tagUpdateRequest, long id) {
        Tag tag = findTagByIdOrThrowResourceNotFoundException(id);
        throwConstraintViolationExceptionIfNameOrSlugAlreadyExists(tagUpdateRequest.getName(),
                tagUpdateRequest.getSlug(), id);
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

    private void throwConstraintViolationExceptionIfNameOrSlugAlreadyExists(String name, String slug) {
        tagRepository.findFirstByNameIgnoreCaseOrSlugIgnoreCase(name, slug)
                .ifPresent(tag -> prepareConstraintViolationExceptionAndThrow(tag, name, slug));
    }

    private void throwConstraintViolationExceptionIfNameOrSlugAlreadyExists(String name, String slug, long idToUpdate) {
        Optional<Tag> tagOptional = tagRepository.findFirstByNameIgnoreCaseOrSlugIgnoreCase(name, slug);

        if (tagOptional.isPresent()) {
            if (tagOptional.get().getId().equals(idToUpdate)) return;
            prepareConstraintViolationExceptionAndThrow(tagOptional.get(), name, slug);
        }
    }

    private void prepareConstraintViolationExceptionAndThrow(Tag savedTag, String name, String slug) {
        boolean isNameViolation = savedTag.getName().equalsIgnoreCase(name);
        boolean isSlugViolation = savedTag.getSlug().equalsIgnoreCase(slug);
        Set<String> violations = new HashSet<>();

        if (isNameViolation) violations.add("name: " + name);
        if (isSlugViolation) violations.add("slug: " + slug);

        String message = "The following attributes already exists in database: " + String.join(", ", violations);

        throw new ConstraintViolationException(message);
    }
}
