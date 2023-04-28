package com.gabrielluciano.blog.dto.post;

import com.gabrielluciano.blog.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostUpdateRequest {

    @NotBlank
    private String title;
    @Pattern(regexp = RegexPatterns.VALID_SLUG_PATTERN, message = "Invalid slug format")
    private String slug;
    private String subtitle;
    private String content;
    private String metaTitle;
    private String metaDescription;
    private String imageUrl;
}
