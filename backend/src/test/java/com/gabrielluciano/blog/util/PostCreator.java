package com.gabrielluciano.blog.util;

import com.gabrielluciano.blog.models.Post;
import com.gabrielluciano.blog.models.Tag;
import com.gabrielluciano.blog.models.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

public class PostCreator {

    private static final long DEFAULT_ID = 1L;
    private static final String DEFAULT_TITLE = "Some Post";
    private static final String DEFAULT_SUBTITLE = "Some Post with awesome content";
    private static final String DEFAULT_CONTENT = "This is the post content";
    private static final String DEFAULT_META_TITLE = "Some Post";
    private static final String DEFAULT_IMAGE_URL = "https://example.com";
    private static final String DEFAULT_META_DESCRIPTION = "Some Post with awesome content";
    private static final String DEFAULT_SLUG = "some-post";
    private static final boolean PUBLISHED = true;
    private static final boolean UNPUBLISHED = false;
    private static final LocalDateTime DEFAULT_DATE = LocalDateTime.now(ZoneOffset.UTC);

    private static final User user = UserCreator.createValidEditorUser();

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

    public static Post createPublishedPostWithAuthorToBeSaved(User author) {
        return createPostToBeSaved(DEFAULT_TITLE, DEFAULT_SLUG, PUBLISHED, null, author);
    }

    public static Post createUnpublishedPostToBeSaved() {
        return createPostToBeSaved(DEFAULT_TITLE, DEFAULT_SLUG, UNPUBLISHED, null);
    }

    public static Post createUnpublishedPostWithAuthorToBeSaved(User author) {
        return createPostToBeSaved(DEFAULT_TITLE, DEFAULT_SLUG, UNPUBLISHED, null, author);
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

    public static Post createPublishedPostWithTitleAndSlugToBeSaved(String title, String slug) {
        return createPostToBeSaved(title, slug, PUBLISHED, null);
    }

    public static Post createPublishedPostWithTitleSlugAndAuthorToBeSaved(String title, String slug, User author) {
        return createPostToBeSaved(title, slug, PUBLISHED, null, author);
    }

    public static Post createPublishedPostWithTagsAndAuthorToBeSaved(Set<Tag> tags, User author) {
        return createPostToBeSaved(DEFAULT_TITLE, DEFAULT_SLUG, PUBLISHED, tags, author);
    }

    public static Post createUnpublishedPostWithTitleAndSlugToBeSaved(String title, String slug) {
        return createPostToBeSaved(title, slug, UNPUBLISHED, null);
    }

    public static Post createUnpublishedPostWithTitleSlugAndAuthorToBeSaved(String title, String slug, User author) {
        return createPostToBeSaved(title, slug, UNPUBLISHED, null, author);
    }

    public static Post createPostToBeSaved(String title, String slug, boolean published, Set<Tag> tags, User author) {
        return createPost(null, title, slug, published, tags, author);
    }

    public static Post createPostToBeSaved(String title, String slug, boolean published, Set<Tag> tags) {
        return createPostToBeSaved(title, slug, published, tags, user);
    }

    public static Post createPost(String title, String slug, boolean published, Set<Tag> tags) {
        return createPost(DEFAULT_ID, title, slug, published, tags, user);
    }

    public static Post createPost(Long id, String title, String slug, boolean published, Set<Tag> tags, User author) {
        LocalDateTime publishedAt = null;
        if (published) {
            publishedAt = DEFAULT_DATE;
        }

        if (tags == null) {
            tags = new HashSet<>();
        }

        return Post.builder()
                .id(id)
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
                .author(author)
                .build();
    }
}
