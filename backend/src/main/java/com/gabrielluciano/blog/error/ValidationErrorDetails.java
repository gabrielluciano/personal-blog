package com.gabrielluciano.blog.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorDetails extends ErrorDetails {

    private String fields;
    private String fieldsMessages;
}
