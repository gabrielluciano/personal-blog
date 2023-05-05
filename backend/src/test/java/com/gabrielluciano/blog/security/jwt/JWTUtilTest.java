package com.gabrielluciano.blog.security.jwt;

import com.gabrielluciano.blog.models.User;
import com.gabrielluciano.blog.util.TestRegexPatterns;
import com.gabrielluciano.blog.util.UserCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Log4j2
class JWTUtilTest {

    @InjectMocks
    private JWTUtil jwtUtil;


    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "API_SECRET", "secret");
    }

    @Test
    @DisplayName("createToken returns valid JWT token when successful")
    void createToken_ReturnsValidJWTToken_WhenSuccessful() {
        User user = UserCreator.createValidUser();

        String token = jwtUtil.createToken(user);

        assertThat(token)
                .isNotNull()
                .matches(TestRegexPatterns.VALID_JWT_PATTERN);

        log.info(token);
    }
}
