package com.gabrielluciano.blog.dto.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {

    private Long id;
    private String name;
    private String slug;
    private String description;
}
