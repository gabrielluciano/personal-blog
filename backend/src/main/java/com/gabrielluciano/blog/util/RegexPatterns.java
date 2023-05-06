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

    /**
     * A regular expression pattern that matches valid email addresses specified in RFC 5322.
     *
     * <p>The regular expression pattern matches an email address that consists of one or more characters from the
     * following set: a-z (lowercase letters), A-Z (uppercase letters), 0-9 (digits), _!#$%&'*+/=?`{|}~^-.</p>
     *
     * <p>The email address must be in the format of local-part@domain, where the local-part can consist of any of the
     * above characters, and the domain is a sequence of one or more characters that consist of a-z (lowercase letters),
     * A-Z (uppercase letters), 0-9 (digits), . (dot), or - (hyphen).</p>
     *
     * <p>The regular expression pattern is:</p>
     *
     * <pre>{@code ^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$}</pre>
     *
     * <p>The pattern consists of the following parts:</p>
     *
     * <ul>
     *     <li>{@code ^} matches the start of the string.</li>
     *     <li>{@code [a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+} matches one or more characters from the specified set.</li>
     *     <li>{@code @} matches the at symbol.</li>
     *     <li>{@code [a-zA-Z0-9.-]+} matches one or more characters that consist of a-z, A-Z, 0-9, . (dot), or - (hyphen).</li>
     *     <li>{@code $} matches the end of the string.</li>
     * </ul>
     *
     * <p>Examples of valid email addresses include:</p>
     *
     * <ul>
     *     <li>"user@example.com"</li>
     *     <li>"user.name@example.com"</li>
     *     <li>"user123@example.com"</li>
     * </ul>
     *
     * <p>Examples of invalid email addresses include:</p>
     *
     * <ul>
     *     <li>"user@"</li>
     *     <li>"@example.com"</li>
     *     <li>"user@.com"</li>
     * </ul>
     */
    public static final String VALID_EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    /**
     * A regular expression pattern that matches a strong password.
     *
     * <p>The regular expression pattern matches any string that:</p>
     * <ul>
     *   <li>Has a minimum length of 8 characters</li>
     *   <li>Contains at least one uppercase letter, one lowercase letter, one digit, and one special character</li>
     *   <li>Allows the following special characters: ! @ # $ % ^ & * ( ) _ + - = { } [ ] ; : ' " , &lt; . &gt; / ?</li>
     * </ul>
     *
     * <p>The regular expression pattern is:</p>
     * <pre>{@code "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_\\+\\-=\\{\\}\\[\\];:'\",\\<.\\>/?]).{8,}$"}</pre>
     *
     * <p>The pattern consists of the following parts:</p>
     * <ul>
     *     <li>{@code ^} matches the start of the string.</li>
     *     <li>{@code (?=.*[A-Z])} matches any string that contains at least one uppercase letter.</li>
     *     <li>{@code (?=.*[a-z])} matches any string that contains at least one lowercase letter.</li>
     *     <li>{@code (?=.*[0-9])} matches any string that contains at least one digit.</li>
     *     <li>{@code (?=.*[!@#$%^&*()_\\+\\-=\\{\\}\\[\\];:'\",\\<.\\>/?])} matches any string that contains at least one of
     *     the allowed special characters.</li>
     *     <li>{@code .{8,}} matches any string that is at least 8 characters long.</li>
     *     <li>{@code $} matches the end of the string.</li>
     * </ul>
     *
     * <p>Examples of strings that match the pattern include:</p>
     * <ul>
     *     <li>"Password1!"</li>
     *     <li>"P@ssword123"</li>
     *     <li>"mySecret&"</li>
     * </ul>
     *
     * <p>Examples of strings that do not match the pattern include:</p>
     * <ul>
     *     <li>"password" (no uppercase letter)</li>
     *     <li>"PASSWORD" (no lowercase letter)</li>
     *     <li>"Password" (no digit)</li>
     *     <li>"Password!" (less than 8 characters long)</li>
     *     <li>"Password123" (no special character)</li>
     *     <li>"password&" (no uppercase letter or digit)</li>
     * </ul>
     */
    public static final String STRONG_PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_\\+\\-=\\{\\}\\[\\];:'\",\\<.\\>/?]).{8,}$";
}
