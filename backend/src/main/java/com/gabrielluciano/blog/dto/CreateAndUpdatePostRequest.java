package com.gabrielluciano.blog.dto;

import com.gabrielluciano.blog.models.entities.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAndUpdatePostRequest {

    private String title;
    private String subtitle;
    private String content;
    private String metaTitle;
    private String metaDescription;
    private String slug;
    private String imageUrl;
    private Long categoryId;
    private Long[] tagsIds;

    public CreateAndUpdatePostRequest() {
    }

    public Post toNewPost() {
        Post post = new Post(title, subtitle, content, metaTitle, metaDescription, slug, imageUrl);
        return post;
    }

    public void updatePostContent(Post post) {
        post.setTitle(title);
        post.setSubtitle(subtitle);
        post.setContent(content);
        post.setMetaTitle(metaTitle);
        post.setMetaDescription(metaDescription);
        post.setSlug(slug);
        post.setImageUrl(imageUrl);
    }
}
