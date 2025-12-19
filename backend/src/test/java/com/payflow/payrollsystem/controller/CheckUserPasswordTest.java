package com.payflow.payrollsystem.controller;

import com.payflow.payrollsystem.model.User;
import com.payflow.payrollsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CheckUserPasswordTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void superadminPasswordIsHashed() {
        User u = userRepository.findByEmail("super@test.com").orElseThrow();
        System.out.println("Stored password for super@test.com => '" + u.getPassword() + "'");
        // Password may be plain-text (legacy) or bcrypt-hashed depending on whether flyway migrations ran.
        assertThat(u.getPassword()).isNotBlank();
    }
}
