package com.gabrielluciano.blog.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagUpdateRequest {

    private String name;
    private String slug;
    private String description;
}
