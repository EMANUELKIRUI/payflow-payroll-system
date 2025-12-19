package com.payflow.payrollsystem.service;

import com.payflow.payrollsystem.model.AuditLog;
import com.payflow.payrollsystem.model.User;
import com.payflow.payrollsystem.repository.AuditLogRepository;
import com.payflow.payrollsystem.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.secret-base64:}")
    private String jwtSecretBase64;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, AuditLogRepository auditLogRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String stored = user.getPassword();

            boolean ok = false;
            if (stored != null && passwordEncoder.matches(password, stored)) {
                ok = true;
            } else if (stored != null && stored.equals(password)) {
                // Legacy plain-text password, migrate to bcrypt
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                ok = true;
            }

            if (ok) {
                // Log successful login
                AuditLog log = new AuditLog();
                log.setUser(user);
                log.setAction("User login");
                log.setTimestamp(LocalDateTime.now());
                auditLogRepository.save(log);
                String token = generateToken(user);
                System.out.println("AuthService: successfully authenticated '" + email + "' and generated token");
                return token;
            }
        }
        throw new BadCredentialsException("Invalid credentials");
    }

    public String registerCompany(String name, String email, String password, String billingPlan) {
        // Create company, user, etc. (simplified)
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("EMPLOYEE");
        user.setEnabled(true);
        userRepository.save(user);
        return "Registered";
    }

    private byte[] getSigningKeyBytes() {
        if (jwtSecretBase64 != null && !jwtSecretBase64.isBlank()) {
            return java.util.Base64.getDecoder().decode(jwtSecretBase64);
        }
        return jwtSecret != null ? jwtSecret.getBytes() : new byte[0];
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole() != null ? user.getRole().toLowerCase() : null)
                .claim("companyId", user.getCompany() != null ? user.getCompany().getId() : null)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(getSigningKeyBytes()), SignatureAlgorithm.HS512)
                .compact();
    }
}