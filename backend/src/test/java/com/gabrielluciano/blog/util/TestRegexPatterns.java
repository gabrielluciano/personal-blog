package com.gabrielluciano.blog.util;

public class TestRegexPatterns {

    /**
     * A regular expression pattern that matches valid JSON Web Tokens (JWTs).
     *
     * <p>The regular expression pattern matches any string that consists of three Base64-encoded parts separated by dots:
     * the header, the payload, and the signature. Each part contains only alphanumeric characters, underscores,
     * and hyphens, and the token ends with zero or more of these characters.</p>
     *
     * <p>The regular expression pattern is:</p>
     *
     * <pre>{@code ^([A-Za-z0-9_-]+\\.){2}[A-Za-z0-9_-]*$}</pre>
     *
     * <p>The pattern consists of the following parts:</p>
     *
     * <ul>
     *     <li>{@code ^} matches the start of the string.</li>
     *     <li>{@code ([A-Za-z0-9_-]+\\.){2}} matches two sequences of one or more alphanumeric characters, underscores,
     *     or hyphens, followed by a dot.</li>
     *     <li>{@code [A-Za-z0-9_-]*} matches zero or more alphanumeric characters, underscores,
     *     or hyphens at the end of the string.</li>
     *     <li>{@code $} matches the end of the string.</li>
     * </ul>
     *
     * <p>Examples of strings that match the pattern include:</p>
     *
     * <ul>
     *     <li>"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"</li>
     * </ul>
     *
     * <p>Examples of strings that do not match the pattern include:</p>
     *
     * <ul>
     *     <li>"my-jwt-string"</li>
     *     <li>"my.jwt.string"</li>
     *     <li>"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ"</li>
     * </ul>
     */
    public static String VALID_JWT_PATTERN = "^([A-Za-z0-9_-]+\\.){2}[A-Za-z0-9_-]*";

}
