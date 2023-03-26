package com.gabrielluciano.blog.error.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorDetails {

    public String title;
    public int status;
    public String message;
    public LocalDateTime timestamp;
    public String path;
}
