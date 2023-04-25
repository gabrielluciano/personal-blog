package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.util.PostCreator;
import com.gabrielluciano.blog.util.TagCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("findAllByPublishedIsTrueAndTitleContainingIgnoreCase returns page of posts containing specified title when successful")
    void findAllByPublishedIsTrueAndTitleContainingIgnoreCase_ReturnsPageOfPostsContainingSpecifiedTitle_WhenSuccessful() {
        String title1 = "Some title";
        String title2 = "Post title";
        String title3 = "Incredible post";

        String slug = "some-slug";
        String titleToSearch = "title";

        Post post1 = postRepository.save(PostCreator.createPublishedPostWithTitleAndSlugToBeSaved(title1, slug));
        Post post2 = postRepository.save(PostCreator.createPublishedPostWithTitleAndSlugToBeSaved(title2, slug));
        postRepository.save(PostCreator.createPublishedPostWithTitleAndSlugToBeSaved(title3, slug));

        Page<Post> page = postRepository.findAllByPublishedIsTrueAndTitleContainingIgnoreCase(titleToSearch, Pageable.unpaged());

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        assertThat(page.getContent().stream()
                .filter(post -> post.getId().equals(post1.getId()))
                .findFirst())
                .isPresent();

        assertThat(page.getContent().stream()
                .filter(post -> post.getId().equals(post2.getId()))
                .findFirst())
                .isPresent();
    }

    @Test
    @DisplayName("findAllByPublishedIsTrueAndTagsId returns page of published posts containing a specific tag when successful")
    void findAllByPublishedIsTrueAndTagsId_ReturnsPageOfPublishedPostsContainingASpecificTag_WhenSuccessful() {
        Tag savedTag1 = tagRepository.save(TagCreator.createTagToBeSaved("some tag 1"));
        Tag savedTag2 = tagRepository.save(TagCreator.createTagToBeSaved("some tag 2"));
        Post savedPost = postRepository.save(PostCreator.createPublishedPostWithTags(Set.of(savedTag1, savedTag2)));

        Page<Post> page = postRepository.findAllByPublishedIsTrueAndTagsId(savedTag1.getId(), Pageable.unpaged());

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(page.getContent().get(0)).isEqualTo(savedPost);
    }

    @Test
    @DisplayName("findAllByPublishedIsTrueAndTagsId returns empty page of posts when no post is found")
    void findAllByPublishedIsTrueAndTagsId_ReturnsEmptyPageOfPosts_WhenNoPostIsFound() {
        postRepository.save(PostCreator.createPublishedPost());

        Page<Post> page = postRepository.findAllByPublishedIsTrueAndTagsId(1L, Pageable.unpaged());

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findAllByPublishedIsTrueAndTitleContainingIgnoreCaseAndTagsId returns page of published posts containing a specific tag and title when successful")
    void findAllByPublishedIsTrueAndTitleContainingIgnoreCaseAndTagsId_ReturnsPageOfPublishedPostsContainingASpecificTagAndTitle_WhenSuccessful() {
        Tag savedTag1 = tagRepository.save(TagCreator.createTagToBeSaved("some tag"));
        Tag savedTag2 = tagRepository.save(TagCreator.createTagToBeSaved("another tag"));

        Post savedPost1 = postRepository.save(PostCreator.createPublishedPostWithTitleAndSlugToBeSaved("rust", "rust"));
        Post savedPost2 = postRepository.save(PostCreator.createPublishedPostWithTitleAndSlugToBeSaved("Java", "java"));
        Post savedPost3 = postRepository.save(PostCreator.createPublishedPostWithTitleAndSlugToBeSaved("java tips", "java-tips"));
        Post savedPost4 = postRepository.save(PostCreator.createPublishedPostWithTitleAndSlugToBeSaved("java guides", "java-guides"));

        savedPost1.setTags(Set.of(savedTag1, savedTag2));
        savedPost2.setTags(Set.of(savedTag1));
        savedPost3.setTags(Set.of(savedTag1));
        savedPost4.setTags(Set.of(savedTag2));

        Page<Post> page = postRepository.findAllByPublishedIsTrueAndTitleContainingIgnoreCaseAndTagsId("java",
                savedTag1.getId(), Pageable.unpaged());

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        assertThat(page.getContent()).contains(savedPost2, savedPost3);
    }

    @Test
    @DisplayName("findAllByPublishedIsTrue returns page of published posts when successful")
    void findAllByPublishedIsTrue_ReturnsPageOfPublishedPosts_WhenSuccessful() {
        Post publishedPostToBeSaved = PostCreator.createPublishedPostToBeSaved();
        Post unpublishedPostToBeSaved = PostCreator.createUnpublishedPostToBeSaved();

        Post publishedPost = postRepository.save(publishedPostToBeSaved);
        postRepository.save(unpublishedPostToBeSaved);

        Page<Post> page = postRepository.findAllByPublishedIsTrue(Pageable.unpaged());

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(page.getContent().get(0)).isEqualTo(publishedPost);
    }

    @Test
    @DisplayName("findAllByPublishedIsTrue returns page of unpublished posts when successful")
    void findAllByPublishedIsTrue_ReturnsPageOfUnpublishedPosts_WhenSuccessful() {
        Post publishedPostToBeSaved = PostCreator.createPublishedPostToBeSaved();
        Post unpublishedPostToBeSaved = PostCreator.createUnpublishedPostToBeSaved();

        postRepository.save(publishedPostToBeSaved);
        Post unpublishedPost = postRepository.save(unpublishedPostToBeSaved);

        Page<Post> page = postRepository.findAllByPublishedIsFalse(Pageable.unpaged());

        assertThat(page).isNotNull();

        assertThat(page.getContent())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(page.getContent().get(0)).isEqualTo(unpublishedPost);
    }

    @Test
    @DisplayName("findByPublishedIsTrueAndSlug returns optional of published post with specific slug when successful")
    void findByPublishedIsTrueAndSlug_ReturnsOptionalOfPublishedPostWithSpecificSlug_WhenSuccessful() {
        String slug = "some-slug";
        Post postToBeSaved = PostCreator.createPublishedPostWithTitleAndSlugToBeSaved("Some title", slug);

        Post expectedPost = postRepository.save(postToBeSaved);

        Optional<Post> optionalPost = postRepository.findByPublishedIsTrueAndSlug(slug);

        assertThat(optionalPost)
                .isNotNull()
                .isPresent()
                .contains(expectedPost);
    }

    @Test
    @DisplayName("findByPublishedIsTrueAndSlug returns optional empty when post is not found or post is not published")
    void findByPublishedIsTrueAndSlug_ReturnsOptionalEmpty_WhenPostIsNotFoundOrPostIsNotPublished() {
        String slug = "non-existent-slug";
        Post postToBeSaved1 = PostCreator.createPublishedPostWithTitleAndSlugToBeSaved("Some title", "some-slug");
        Post postToBeSaved2 = PostCreator.createUnpublishedPostWithTitleAndSlugToBeSaved("Some title", slug);

        postRepository.save(postToBeSaved1);
        postRepository.save(postToBeSaved2);

        Optional<Post> optionalPost = postRepository.findByPublishedIsTrueAndSlug(slug);

        assertThat(optionalPost)
                .isNotNull()
                .isEmpty();
    }
}
