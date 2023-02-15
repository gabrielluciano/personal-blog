package com.gabrielluciano.blog.repositories;

import com.gabrielluciano.blog.models.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
