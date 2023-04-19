package com.gabrielluciano.blog.dto.post;

import com.gabrielluciano.blog.dto.tag.TagResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String subtitle;
    private String content;
    private String metaTitle;
    private String metaDescription;
    private String slug;
    private String imageUrl;
    private Boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private Set<TagResponse> tags;
}
