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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@SequenceGenerator(
        name = Post.SEQUENCE_NAME,
        sequenceName = Post.SEQUENCE_NAME,
        allocationSize = 50, initialValue = 1)
@Getter
@Setter
@NoArgsConstructor
public class Post extends AbstractPersistentObject {

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

    public void addTag(Tag tag) {
        if (!tags.contains(tag)) {
            this.tags.add(tag);
            tag.addPost(this);
        }
        this.equals(this);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}
