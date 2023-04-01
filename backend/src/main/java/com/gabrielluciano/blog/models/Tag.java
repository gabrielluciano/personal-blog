package com.gabrielluciano.blog.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tags")
@SequenceGenerator(
        name = Tag.SEQUENCE_NAME,
        sequenceName = Tag.SEQUENCE_NAME
)
@Getter
@Setter
public class Tag extends AbstractPersistentObject {

    public static final String SEQUENCE_NAME = "SEQUENCE_TAG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;
    private String name;
    private String slug;
    private String description;

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
