package com.gabrielluciano.blog.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "post_comments")
@SequenceGenerator(
        name = PostComment.SEQUENCE_NAME,
        sequenceName = PostComment.SEQUENCE_NAME
)
@Getter
@Setter
@NoArgsConstructor
public class PostComment extends AbstractPersistentObject {

    public static final String SEQUENCE_NAME = "SEQUENCE_POSTCOMMENT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;
    @Column(length = 300, nullable = false)
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCommentStatus status;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Post post;

    @ManyToOne(optional = false)
    private User user;

    public PostComment(String content) {
        this.content = content;
        status = PostCommentStatus.WAITING_FOR_APPROVAL;
        createdAt = LocalDateTime.now(ZoneOffset.UTC);
        updatedAt = LocalDateTime.now(ZoneOffset.UTC);
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
