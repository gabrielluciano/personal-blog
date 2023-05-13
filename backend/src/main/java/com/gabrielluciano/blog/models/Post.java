package com.gabrielluciano.blog.models;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@SequenceGenerator(name = Post.SEQUENCE_NAME, sequenceName = Post.SEQUENCE_NAME)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends AbstractPersistentObject {

    public static final String SEQUENCE_NAME = "SEQUENCE_POST";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;
    private String title;
    private String subtitle;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String metaTitle;
    private String metaDescription;
    private String slug;
    private String imageUrl;
    private Boolean published;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime publishedAt;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    private User author;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", slug='" + slug + '\'' +
                ", published=" + published +
                '}';
    }
}
