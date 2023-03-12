package com.gabrielluciano.blog.error.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
public class ErrorDetails {

    public String title;
    public int status;
    public String message;
    public LocalDateTime timestamp = LocalDateTime.now(ZoneOffset.UTC);
    public String path;

    public ErrorDetails withTitle(String title) {
        this.title = title;
        return this;
    }

    public ErrorDetails withStatus(int status) {
        this.status = status;
        return this;
    }

    public ErrorDetails withMessage(String message) {
        this.message = message;
        return this;
    }

    public ErrorDetails withPath(String path) {
        this.path = path;
        return this;
    }
}
