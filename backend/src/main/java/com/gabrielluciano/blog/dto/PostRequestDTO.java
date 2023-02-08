package com.gabrielluciano.blog.dto;

import com.gabrielluciano.blog.models.entities.Post;

public class PostRequestDTO {

    private String title;
    private String subtitle;
    private String content;
    private String metaTitle;
    private String metaDescription;
    private String slug;
    private String imageUrl;
    private Long authorId;
    private Long categoryId;
    private Long[] tagsIds;

    public PostRequestDTO() {
    }

    public Post toNewPost() {
        Post post = new Post(title, subtitle, content, metaTitle, metaDescription, slug, imageUrl);
        return post;
    }

    public void fillPost(Post post) {
        post.setTitle(title);
        post.setSubtitle(subtitle);
        post.setContent(content);
        post.setMetaTitle(metaTitle);
        post.setMetaDescription(metaDescription);
        post.setSlug(slug);
        post.setImageUrl(imageUrl);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long[] getTagsIds() {
        return tagsIds;
    }

    public void setTagsIds(Long[] tagsIds) {
        this.tagsIds = tagsIds;
    }
}
