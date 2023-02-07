package com.gabrielluciano.blog.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "categories")
@SequenceGenerator(
        name = Category.SEQUENCE_NAME,
        sequenceName = Category.SEQUENCE_NAME
)
public class Category {

    public static final String SEQUENCE_NAME = "SEQUENCE_CATEGORY";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 50)
    private String slug;
    private String description;

    public Category() {
    }

    public Category(String name, String slug, String description) {
        this.name = name;
        this.slug = slug;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id)
                && Objects.equals(name, category.name)
                && Objects.equals(slug, category.slug)
                && Objects.equals(description, category.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, slug, description);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
