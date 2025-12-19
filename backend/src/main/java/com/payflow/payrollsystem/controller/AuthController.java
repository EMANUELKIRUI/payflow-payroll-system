package com.payflow.payrollsystem.controller;

import com.payflow.payrollsystem.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String token = authService.authenticate(credentials.get("email"), credentials.get("password"));
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> data) {
        String result = authService.registerCompany(data.get("name"), data.get("email"), data.get("password"), data.get("billingPlan"));
        return ResponseEntity.ok(Map.of("message", result));
    }
}