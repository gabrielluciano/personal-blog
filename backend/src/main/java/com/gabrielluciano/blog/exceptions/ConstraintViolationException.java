package com.gabrielluciano.blog.exceptions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConstraintViolationException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "The following attributes already exist in one or more entries in the database table '%s' => ";

    public ConstraintViolationException(String tableName, Map<String, String> violations) {
        super(String.format(DEFAULT_MESSAGE, tableName) + getViolationsMessage(violations));
    }

    private static String getViolationsMessage(Map<String, String> violations) {
        Set<String> keys = violations.keySet();
        Set<String> violationMessages = new HashSet<>();
        for (String key : keys) {
            violationMessages.add(key + ": " + violations.get(key));
        }
        return String.join(", ", violationMessages);
    }
}
