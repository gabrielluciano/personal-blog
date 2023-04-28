package com.gabrielluciano.blog.util;

public final class RegexPatterns {

    private RegexPatterns() {
    }

    /**
     * A regular expression pattern that matches valid post slugs.
     *
     * <p>The regular expression pattern matches any string that starts with one or more alphanumeric characters,
     * followed by zero or more groups of a hyphen followed by one or more alphanumeric characters, and ends with
     * an alphanumeric character. This allows for slugs that contain only alphanumeric characters and hyphens, and
     * do not start or end with a hyphen. It also allows for multiple hyphens in a row, but not spaces or other
     * special characters.</p>
     *
     * <p>The regular expression pattern is:</p>
     *
     * <pre>{@code ^[a-zA-Z0-9]+(-[a-zA-Z0-9]+)*$}</pre>
     *
     * <p>The pattern consists of the following parts:</p>
     *
     * <ul>
     *     <li>{@code ^} matches the start of the string.</li>
     *     <li>{@code [a-zA-Z0-9]+} matches one or more alphanumeric characters.</li>
     *     <li>{@code (-[a-zA-Z0-9]+)*} matches zero or more groups of a hyphen followed by one or more alphanumeric
     *     characters. The parentheses and asterisk indicate that this group can occur zero or more times.</li>
     *     <li>{@code -} matches a hyphen.</li>
     *     <li>{@code $} matches the end of the string.</li>
     * </ul>
     *
     * <p>Examples of strings that match the pattern include:</p>
     *
     * <ul>
     *     <li>"my-post-slug"</li>
     *     <li>"another-post-123"</li>
     *     <li>"some-longer-post-slug-123"</li>
     * </ul>
     *
     * <p>Examples of strings that do not match the pattern include:</p>
     *
     * <ul>
     *     <li>"-post-slug"</li>
     *     <li>"post-slug-"</li>
     *     <li>"post slug"</li>
     * </ul>
     */
    public static final String VALID_SLUG_PATTERN = "^[a-zA-Z0-9]+(-[a-zA-Z0-9]+)*$";
}
