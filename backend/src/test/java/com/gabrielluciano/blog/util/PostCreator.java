package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.models.Tag;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

public class PostCreator {

    public static long DEFAULT_ID = 1L;
    public static String DEFAULT_TITLE = "Some Post";
    public static String DEFAULT_SUBTITLE = "Some Post with awesome content";
    public static String DEFAULT_CONTENT = "This is the post content";
    public static String DEFAULT_META_TITLE = "Some Post";
    public static String DEFAULT_IMAGE_URL = "https://example.com";
    public static String DEFAULT_META_DESCRIPTION = "Some Post with awesome content";
    public static String DEFAULT_SLUG = "some-post";
    public static boolean PUBLISHED = true;
    public static boolean UNPUBLISHED = false;

    private static final LocalDateTime DEFAULT_DATE = LocalDateTime.now(ZoneOffset.UTC);

    private PostCreator() {
    }

    public static Post createPublishedPost() {
        return createPost(DEFAULT_TITLE, DEFAULT_SLUG, PUBLISHED, null);
    }

    public static Post createUnpublishedPost() {
        return createPost(DEFAULT_TITLE, DEFAULT_SLUG, UNPUBLISHED, null);
    }

    public static Post createPublishedPostToBeSaved() {
        return createPostToBeSaved(DEFAULT_TITLE, DEFAULT_SLUG, PUBLISHED, null);
    }

    public static Post createUnpublishedPostToBeSaved() {
        return createPostToBeSaved(DEFAULT_TITLE, DEFAULT_SLUG, UNPUBLISHED, null);
    }

    public static Post createPublishedPostWithTags(Set<Tag> tags) {
        return createPost(DEFAULT_TITLE, DEFAULT_SLUG, PUBLISHED, tags);
    }

    public static Post createUnpublishedPostWithTags(Set<Tag> tags) {
        return createPost(DEFAULT_TITLE, DEFAULT_SLUG, UNPUBLISHED, tags);
    }

    public static Post createPublishedPostWithTagsToBeSaved(Set<Tag> tags) {
        return createPostToBeSaved(DEFAULT_TITLE, DEFAULT_SLUG, PUBLISHED, tags);
    }

    public static Post createPublishedPostWithTitleAndSlug(String title, String slug) {
        return createPost(title, slug, PUBLISHED, null);
    }

    public static Post createUnpublishedPostWithTitleAndSlug(String title, String slug) {
        return createPost(title, slug, UNPUBLISHED, null);
    }

    public static Post createPublishedPostWithTitleAndSlugToBeSaved(String title, String slug) {
        return createPostToBeSaved(title, slug, PUBLISHED, null);
    }

    public static Post createUnpublishedPostWithTitleAndSlugToBeSaved(String title, String slug) {
        return createPostToBeSaved(title, slug, UNPUBLISHED, null);
    }

    public static Post createPostToBeSaved(String title, String slug, boolean published, Set<Tag> tags) {
        Post post = createPost(title, slug, published, tags);
        post.setId(null);
        return post;
    }

    public static Post createPost(String title, String slug, boolean published, Set<Tag> tags) {
        LocalDateTime publishedAt = null;
        if (published) {
             publishedAt = DEFAULT_DATE;
        }

        if (tags == null) {
            tags = new HashSet<>();
        }

        return Post.builder()
                .id(DEFAULT_ID)
                .title(title == null ? DEFAULT_TITLE : title)
                .subtitle(DEFAULT_SUBTITLE)
                .content(DEFAULT_CONTENT)
                .metaTitle(DEFAULT_META_TITLE)
                .metaDescription(DEFAULT_META_DESCRIPTION)
                .slug(slug == null ? DEFAULT_SLUG : slug)
                .imageUrl(DEFAULT_IMAGE_URL)
                .published(published)
                .createdAt(DEFAULT_DATE)
                .updatedAt(DEFAULT_DATE)
                .publishedAt(publishedAt)
                .tags(tags)
                .build();
    }
}
