package com.payflow.payrollsystem.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenRoundTripTest {

    @Autowired
    private AuthService authService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Test
    public void tokenRoundTrip_parsesSuccessfully() {
        String token = authService.authenticate("super@test.com", "password");
        assertThat(token).isNotNull();
        assertThat(token.split("\\.").length).isEqualTo(3);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo("super@test.com");
        assertThat(claims.get("role", String.class)).isEqualTo("superadmin");
    }
}
