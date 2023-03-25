package com.gabrielluciano.blog.dto.tag;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagUpdateRequest {

    private Long id;
    private String name;
    private String slug;
    private String description;
}
