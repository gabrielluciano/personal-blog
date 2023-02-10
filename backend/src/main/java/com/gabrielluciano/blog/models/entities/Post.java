package com.gabrielluciano.blog.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "posts")
@SequenceGenerator(
        name = Post.SEQUENCE_NAME,
        sequenceName = Post.SEQUENCE_NAME
)
public class Post {

    public static final String SEQUENCE_NAME = "SEQUENCE_POST";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String subtitle;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String metaTitle;
    @Column(length = 500)
    private String metaDescription;
    @Column(nullable = false)
    private String slug;
    @Column(length = 500)
    private String imageUrl;
    @Column(nullable = false)
    private Boolean published;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime publishedAt;

    @ManyToOne(optional = false)
    private User author;

    @ManyToOne(optional = false)
    private Category category;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    public Post() {
    }

    public Post(String title, String subtitle, String content, String metaTitle, String metaDescription, String slug, String imageUrl) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.metaTitle = metaTitle;
        this.metaDescription = metaDescription;
        this.slug = slug;
        this.imageUrl = imageUrl;
        published = false;
        createdAt = LocalDateTime.now(ZoneOffset.UTC);
        updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean isPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        if (!tags.contains(tag)) {
            this.tags.add(tag);
            tag.addPost(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id)
                && Objects.equals(title, post.title)
                && Objects.equals(subtitle, post.subtitle)
                && Objects.equals(content, post.content)
                && Objects.equals(metaTitle, post.metaTitle)
                && Objects.equals(metaDescription, post.metaDescription)
                && Objects.equals(slug, post.slug)
                && Objects.equals(imageUrl, post.imageUrl)
                && Objects.equals(published, post.published)
                && Objects.equals(createdAt, post.createdAt)
                && Objects.equals(updatedAt, post.updatedAt)
                && Objects.equals(publishedAt, post.publishedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, subtitle, content, metaTitle, metaDescription, slug, imageUrl, published, createdAt, updatedAt, publishedAt);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", content='" + content + '\'' +
                ", metaTitle='" + metaTitle + '\'' +
                ", metaDescription='" + metaDescription + '\'' +
                ", slug='" + slug + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", published=" + published +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", publishedAt=" + publishedAt +
                '}';
    }
}
