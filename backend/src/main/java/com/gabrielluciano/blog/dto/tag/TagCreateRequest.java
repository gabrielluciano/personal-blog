package com.gabrielluciano.blog.dto.tag;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagCreateRequest {

    private String name;
    private String slug;
    private String description;

}
