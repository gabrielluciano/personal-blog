package com.gabrielluciano.blog.error.exceptions;

public class InvalidPostCommentStatusException extends RuntimeException {

    public InvalidPostCommentStatusException() {
        super("Invalid post comment status format. Options are: 'approved', 'rejected' and 'waiting'");
    }
}
