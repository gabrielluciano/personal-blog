package com.gabrielluciano.blog.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

    @Test
    @DisplayName("verifyToken returns decodedJWT when successful")
    void verifyToken_ReturnsDecodedJWT_WhenSuccessful() {
        User user = UserCreator.createValidUser();

        String token = jwtUtil.createToken(user);

        DecodedJWT decodedJWT = jwtUtil.verifyToken(token);

        assertThat(decodedJWT).isNotNull();

        assertThat(decodedJWT.getClaim("id").asLong()).isEqualTo(user.getId());

        assertThat(decodedJWT.getClaim("email").asString()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("verifyToken returns throws JWTVerificationException when token is not valid")
    void verifyToken_ThrowsJWTVerificationException_WhenTokenIsNotValid() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        assertThatExceptionOfType(JWTVerificationException.class)
                .isThrownBy(() -> jwtUtil.verifyToken(token));
    }

    @Test
    @DisplayName("verifyToken returns throws JWTVerificationException when token is expired")
    void verifyToken_ThrowsJWTVerificationException_WhenTokenIExpired() {
        ReflectionTestUtils.setField(jwtUtil, "tokenDurationInDays", 0L);

        User user = UserCreator.createValidUser();

        String token = jwtUtil.createToken(user);

        assertThatExceptionOfType(JWTVerificationException.class)
                .isThrownBy(() -> jwtUtil.verifyToken(token));
    }
}
