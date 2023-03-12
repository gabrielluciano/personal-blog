package com.gabrielluciano.blog.services;

import com.gabrielluciano.blog.error.exceptions.ResourceNotFoundException;
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

    public Optional<Tag> findOptionalTagById(Long id) {
        return tagRepository.findById(id);
    }

    public Tag findTagById(Long id) {
        return findOptionalTagById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Tag.class, id));
    }

    public Page<Tag> findTagsPaginated(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    public Tag createTag(Tag tag) {
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
