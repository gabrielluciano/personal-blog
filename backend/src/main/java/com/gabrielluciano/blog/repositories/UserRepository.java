package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
