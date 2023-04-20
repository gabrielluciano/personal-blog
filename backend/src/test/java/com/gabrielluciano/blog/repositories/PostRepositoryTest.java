package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.util.PostCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("findByTitleContainingIgnoreCase returns page of posts containing specified title when successful")
    void findByTitleContainingIgnoreCase_ReturnsPageOfPostsContainingSpecifiedTitle_WhenSuccessful() {
        String title1 = "Some title";
        String title2 = "Post title";
        String title3 = "Incredible post";

        String slug = "some-slug";
        String titleToSearch = "title";

        Post post1 = postRepository.save(PostCreator.createPublishedPostWithTitleAndSlugToBeSaved(title1, slug));
        Post post2 = postRepository.save(PostCreator.createPublishedPostWithTitleAndSlugToBeSaved(title2, slug));
        postRepository.save(PostCreator.createPublishedPostWithTitleAndSlugToBeSaved(title3, slug));

        Page<Post> page = postRepository.findByTitleContainingIgnoreCase(titleToSearch, PageRequest.of(0, 10));

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
}
