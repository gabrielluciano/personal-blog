package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.models.User;
import com.gabrielluciano.blog.util.PostCreator;
import com.gabrielluciano.blog.util.TagCreator;
import com.gabrielluciano.blog.util.UserCreator;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private UserRepository userRepository;

    private User author;

    @BeforeEach
    void setUp() {
        author = userRepository.save(UserCreator.createValidEditorUserToBeSaved());
    }

    @Test
    @DisplayName("findAllByPublishedIsTrueAndTitleContainingIgnoreCase returns page of posts containing specified title when successful")
    void findAllByPublishedIsTrueAndTitleContainingIgnoreCase_ReturnsPageOfPostsContainingSpecifiedTitle_WhenSuccessful() {
        String title1 = "Some title";
        String title2 = "Post title";
        String title3 = "Incredible post";

        String slug = "some-slug";
        String titleToSearch = "title";

        Post post1 = postRepository.save(PostCreator.createPublishedPostWithTitleSlugAndAuthorToBeSaved(title1, slug, author));
        Post post2 = postRepository.save(PostCreator.createPublishedPostWithTitleSlugAndAuthorToBeSaved(title2, slug, author));
        postRepository.save(PostCreator.createPublishedPostWithTitleSlugAndAuthorToBeSaved(title3, slug, author));

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
        Post savedPost = postRepository.save(PostCreator
                .createPublishedPostWithTagsAndAuthorToBeSaved(Set.of(savedTag1, savedTag2), author));

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
        postRepository.save(PostCreator.createPublishedPostWithAuthorToBeSaved(author));

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

        Post savedPost1 = postRepository.save(PostCreator
                .createPublishedPostWithTitleSlugAndAuthorToBeSaved("rust", "rust", author));
        Post savedPost2 = postRepository.save(PostCreator
                .createPublishedPostWithTitleSlugAndAuthorToBeSaved("Java", "java", author));
        Post savedPost3 = postRepository.save(PostCreator
                .createPublishedPostWithTitleSlugAndAuthorToBeSaved("java tips", "java-tips", author));
        Post savedPost4 = postRepository.save(PostCreator
                .createPublishedPostWithTitleSlugAndAuthorToBeSaved("java guides", "java-guides", author));

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
        Post publishedPostToBeSaved = PostCreator.createPublishedPostWithAuthorToBeSaved(author);
        Post unpublishedPostToBeSaved = PostCreator.createUnpublishedPostWithAuthorToBeSaved(author);

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
        Post publishedPostToBeSaved = PostCreator.createPublishedPostWithAuthorToBeSaved(author);
        Post unpublishedPostToBeSaved = PostCreator.createUnpublishedPostWithAuthorToBeSaved(author);

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
        Post postToBeSaved = PostCreator.createPublishedPostWithTitleSlugAndAuthorToBeSaved("Some title", slug, author);

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
        Post postToBeSaved1 = PostCreator
                .createPublishedPostWithTitleSlugAndAuthorToBeSaved("Some title", "some-slug", author);
        Post postToBeSaved2 = PostCreator
                .createUnpublishedPostWithTitleSlugAndAuthorToBeSaved("Some title", slug, author);

        postRepository.save(postToBeSaved1);
        postRepository.save(postToBeSaved2);

        Optional<Post> optionalPost = postRepository.findByPublishedIsTrueAndSlug(slug);

        assertThat(optionalPost)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findFirstByTitleIgnoreCaseOrSlugIgnoreCase returns optional of post when post with same title is found")
    void findFirstByTitleIgnoreCaseOrSlugIgnoreCase_ReturnsOptionalOfPost_WhenPostWithSameTitleIsFound() {
        String expectedTitle = "Some post";

        Post savedPost = postRepository.save(PostCreator
                .createPublishedPostWithTitleSlugAndAuthorToBeSaved(expectedTitle, "some-slug", author));

        Optional<Post> postOptional = postRepository.findFirstByTitleIgnoreCaseOrSlugIgnoreCase(expectedTitle, "wrong slug");

        assertThat(postOptional)
                .isNotNull()
                .isPresent()
                .contains(savedPost);
    }

    @Test
    @DisplayName("findFirstByTitleIgnoreCaseOrSlugIgnoreCase returns optional of post when post with same slug is found")
    void findFirstByTitleIgnoreCaseOrSlugIgnoreCase_ReturnsOptionalOfPost_WhenPostWithSameSlugIsFound() {
        String expectedSlug = "some-slug";

        Post savedPost = postRepository.save(PostCreator
                .createPublishedPostWithTitleSlugAndAuthorToBeSaved("Some title", expectedSlug, author));

        Optional<Post> postOptional = postRepository.findFirstByTitleIgnoreCaseOrSlugIgnoreCase("Wrong title", expectedSlug);

        assertThat(postOptional)
                .isNotNull()
                .isPresent()
                .contains(savedPost);
    }

    @Test
    @DisplayName("findFirstByTitleIgnoreCaseOrSlugIgnoreCase returns optional empty when no post is found")
    void findFirstByTitleIgnoreCaseOrSlugIgnoreCase_ReturnsOptionalEmpty_WhenNoPostIsFound() {
        String wrongTitle = "Wrong title";
        String wrongSlug = "wrong-slug";

        Post savedPost = postRepository.save(PostCreator.createPublishedPostWithAuthorToBeSaved(author));

        Optional<Post> postOptional = postRepository.findFirstByTitleIgnoreCaseOrSlugIgnoreCase(wrongTitle, wrongSlug);

        assertThat(savedPost.getTitle()).isNotEqualTo(wrongTitle);

        assertThat(savedPost.getSlug()).isNotEqualTo(wrongSlug);

        assertThat(postOptional)
                .isNotNull()
                .isEmpty();
    }
}
