package com.gabrielluciano.blog.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagUpdateRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String slug;
    private String description;
}
