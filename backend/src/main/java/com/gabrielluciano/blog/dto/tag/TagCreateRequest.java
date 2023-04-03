package com.gabrielluciano.blog.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagCreateRequest {

    private String name;
    private String slug;
    private String description;
}
