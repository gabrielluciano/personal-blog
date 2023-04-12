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
        Optional<Tag> tagOptional = tagRepository.findFirstByNameIgnoreCaseOrSlugIgnoreCase(name, slug);

        if (tagOptional.isPresent()) {
            boolean isNameViolation = tagOptional.get().getName().equalsIgnoreCase(name);
            boolean isSlugViolation = tagOptional.get().getSlug().equalsIgnoreCase(slug);
            Set<String> violations = new HashSet<>();

            if (isNameViolation) violations.add("name: " + name);
            if (isSlugViolation) violations.add("slug: " + slug);

            String message = "The following attributes already exists in database: " + String.join(", ", violations);

            throw new ConstraintViolationException(message);
        }
    }
}
