package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
