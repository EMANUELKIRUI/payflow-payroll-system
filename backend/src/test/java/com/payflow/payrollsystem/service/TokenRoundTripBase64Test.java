package com.payflow.payrollsystem.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenRoundTripBase64Test {

    @Autowired
    private AuthService authService;

    private static final String RAW_SECRET = "long-secret-for-testing-purpose-only-repeat-to-reach-64-bytes-0000000000";

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        String base64 = Base64.getEncoder().encodeToString(RAW_SECRET.getBytes());
        registry.add("jwt.secret-base64", () -> base64);
    }

    @Test
    public void tokenRoundTrip_base64Secret_parsesSuccessfully() {
        String token = authService.authenticate("super@test.com", "password");
        assertThat(token).isNotNull();
        assertThat(token.split("\\.").length).isEqualTo(3);

        byte[] keyBytes = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(RAW_SECRET.getBytes()));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo("super@test.com");
        assertThat(claims.get("role", String.class)).isEqualTo("superadmin");
    }
}
