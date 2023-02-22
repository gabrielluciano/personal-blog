package com.gabrielluciano.blog.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@SequenceGenerator(
        name = Category.SEQUENCE_NAME,
        sequenceName = Category.SEQUENCE_NAME
)
@Getter
@Setter
@NoArgsConstructor
public class Category extends AbstractPersistentObject {

    public static final String SEQUENCE_NAME = "SEQUENCE_CATEGORY";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;
    @Column(nullable = false, length = 50, unique = true)
    private String name;
    @Column(nullable = false, length = 50, unique = true)
    private String slug;
    private String description;

    public Category(String name, String slug, String description) {
        this.name = name;
        this.slug = slug;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}
