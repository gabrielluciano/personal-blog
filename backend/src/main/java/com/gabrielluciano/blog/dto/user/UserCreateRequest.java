package com.gabrielluciano.blog.dto.user;

import com.gabrielluciano.blog.util.RegexPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

    private static final String PASSWORD_VALIDATION_MESSAGE = "The password must be at least 8 characters long and contain at least one * uppercase letter, one lowercase letter, one digit, and one special character.";

    @NotBlank
    private String name;
    @Email(regexp = RegexPatterns.VALID_EMAIL_PATTERN)
    private String email;
    @Pattern(message = PASSWORD_VALIDATION_MESSAGE, regexp = RegexPatterns.STRONG_PASSWORD_PATTERN)
    private String password;
}
