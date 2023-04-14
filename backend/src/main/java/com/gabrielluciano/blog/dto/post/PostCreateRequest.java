package com.gabrielluciano.blog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCreateRequest {

    private String title;
    private String subtitle;
    private String content;
    private String metaTitle;
    private String metaDescription;
    private String slug;
    private String imageUrl;
}
