package com.payflow.payrollsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AuthServiceSmokeTest {

    @Autowired
    private AuthService authService;

    @Test
    public void authenticateSuperadmin_direct() {
        String token = authService.authenticate("super@test.com", "password");
        System.out.println("AuthService returned token: " + token);
        assertThat(token).isNotNull();
        assertThat(token.split("\\.").length).isEqualTo(3);
    }
}
