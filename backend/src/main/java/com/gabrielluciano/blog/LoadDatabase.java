package com.gabrielluciano.blog;

import com.gabrielluciano.blog.models.Role;
import com.gabrielluciano.blog.models.entities.Category;
import com.gabrielluciano.blog.models.entities.Post;
import com.gabrielluciano.blog.models.entities.Tag;
import com.gabrielluciano.blog.models.entities.User;
import com.gabrielluciano.blog.repositories.CategoryRepository;
import com.gabrielluciano.blog.repositories.PostRepository;
import com.gabrielluciano.blog.repositories.TagRepository;
import com.gabrielluciano.blog.repositories.UserRepository;
import com.gabrielluciano.blog.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(
            PostRepository postRepository,
            CategoryRepository categoryRepository,
            TagRepository tagRepository,
            UserService userService) {

        return args -> {
            User user1 = new User("João", "joao@email.com", "123");
            user1.addRole(Role.WRITER, Role.ADMIN);
            User user2 = new User("Maria", "maria@email.com", "123");
            User user3 = new User("Not Author User", "notauthor@email.com", "123");
            user3.addRole(Role.ADMIN);

            Category category1 = new Category("News", "news", "Some news");
            Category category2 = new Category("Tutorial", "tutorial", "Some tutorials");

            Tag tag1 = new Tag("Java", "java", "Java Programming Language");
            Tag tag2 = new Tag("Programming", "programming", "About programming");

            Post post1 = new Post(
                    "Título 1",
                    "Subtítulo 1",
                    "Conteúdo 1",
                    "Meta título 1",
                    "Meta descrição 1",
                    "titulo-1",
                    "http://titlo-1"
            );
            post1.setAuthor(user1);
            post1.setCategory(category2);
            post1.addTag(tag1);

            Post post2 = new Post(
                    "Título 2",
                    "Subtítulo 2",
                    "Conteúdo 2",
                    "Meta título 2",
                    "Meta descrição 2",
                    "titulo-dois",
                    "http://titlo-2"
            );
            post2.setAuthor(user2);
            post2.setCategory(category1);
            post2.addTag(tag1);
            post2.addTag(tag2);

            userService.createUser(user1);
            userService.createUser(user2);
            userService.createUser(user3);

            categoryRepository.save(category1);
            categoryRepository.save(category2);

            tagRepository.save(tag1);
            tagRepository.save(tag2);

            postRepository.save(post1);
            postRepository.save(post2);
        };
    }
}
