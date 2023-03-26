package com.gabrielluciano.blog.dto.post;

import com.gabrielluciano.blog.models.entities.Category;
import com.gabrielluciano.blog.models.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostPagedResponse {

    private Long id;
    private String title;
    private String subtitle;
    private String metaTitle;
    private String metaDescription;
    private String slug;
    private String imageUrl;
    private Boolean published;
    private LocalDateTime publishedAt;
    private User author;
    private Category category;
}
