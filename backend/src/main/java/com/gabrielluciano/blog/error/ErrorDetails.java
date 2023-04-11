package com.gabrielluciano.blog.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorDetails {

    private String title;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;
}
