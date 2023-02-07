package com.gabrielluciano.blog.models.entities;

import com.gabrielluciano.blog.models.PostCommentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "post_comments")
@SequenceGenerator(
        name = PostComment.SEQUENCE_NAME,
        sequenceName = PostComment.SEQUENCE_NAME
)
public class PostComment {

    public static final String SEQUENCE_NAME = "SEQUENCE_POSTCOMMENT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;
    @Column(length = 300, nullable = false)
    private String content;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCommentStatus status;

    @ManyToOne(optional = false)
    private Post post;

    @ManyToOne(optional = false)
    private User user;

    public PostComment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PostCommentStatus getStatus() {
        return status;
    }

    public void setStatus(PostCommentStatus status) {
        this.status = status;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostComment that = (PostComment) o;
        return Objects.equals(id, that.id)
                && Objects.equals(content, that.content)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt)
                && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, createdAt, updatedAt, status);
    }

    @Override
    public String toString() {
        return "PostComment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status=" + status +
                '}';
    }
}
