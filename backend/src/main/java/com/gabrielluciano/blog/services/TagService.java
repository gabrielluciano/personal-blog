package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.exceptions.PaginationException;
import com.gabrielluciano.blog.exceptions.TagAlreadyExistsException;
import com.gabrielluciano.blog.exceptions.TagNotFoundException;
import com.gabrielluciano.blog.models.entities.Tag;
import com.gabrielluciano.blog.repositories.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag findTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
    }

    public Page<Tag> findTagsPaginated(Integer page, Integer size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return tagRepository.findAll(pageable);
        } catch (IllegalArgumentException ex) {
            throw new PaginationException(page, size);
        }
    }

    public Tag createTag(Tag tag) {
        Optional<Tag> tagOptional = tagRepository.findOneByNameOrSlug(tag.getName(), tag.getSlug());
        if (tagOptional.isPresent())
            throw new TagAlreadyExistsException(tag);
        return tagRepository.save(tag);
    }

    public Tag updateTag(Tag tag, Long id) {
        Tag tagFromDb = findTagById(id);

        tagFromDb.setName(tag.getName());
        tagFromDb.setDescription(tag.getDescription());
        tagFromDb.setSlug(tag.getSlug());

        return tagRepository.save(tagFromDb);
    }

    public void deleteTagById(Long id) {
        Tag tagFromDb = findTagById(id);
        tagRepository.deleteById(id);
    }
}
