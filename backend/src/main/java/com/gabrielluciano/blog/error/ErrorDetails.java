package com.gabrielluciano.blog.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {

    private String title;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;
}
