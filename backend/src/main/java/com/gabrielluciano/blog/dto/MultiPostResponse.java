package com.gabrielluciano.blog.dto;

import com.gabrielluciano.blog.models.entities.Category;
import com.gabrielluciano.blog.models.entities.Post;
import com.gabrielluciano.blog.models.entities.Tag;
import com.gabrielluciano.blog.models.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class MultiPostResponse {

    private Long id;
    private String title;
    private String subtitle;
    private String metaTitle;
    private String metaDescription;
    private String slug;
    private String imageUrl;
    private Boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private User author;
    private Category category;
    private Set<Tag> tags = new HashSet<>();

    public MultiPostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.subtitle = post.getSubtitle();
        this.metaTitle = post.getMetaTitle();
        this.metaDescription = post.getMetaDescription();
        this.slug = post.getSlug();
        this.imageUrl = post.getImageUrl();
        this.published = post.getPublished();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.publishedAt = post.getPublishedAt();
        this.author = post.getAuthor();
        this.category = post.getCategory();
        this.tags = post.getTags();
    }

}
