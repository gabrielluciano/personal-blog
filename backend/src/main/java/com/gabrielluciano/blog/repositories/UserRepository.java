package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
