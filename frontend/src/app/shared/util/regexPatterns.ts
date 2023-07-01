/**
 * The regular expression pattern for validating slugs.
 * A valid slug consists of alphanumeric characters and hyphens, with each hyphen separated alphanumeric group.
 * Example: my-slug, another-slug-123
 *
 * Regex pattern: ^[a-zA-Z0-9]+(-[a-zA-Z0-9]+)*$
 */
export const VALID_SLUG_PATTERN = '^[a-zA-Z0-9]+(-[a-zA-Z0-9]+)*$';
