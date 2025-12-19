package com.payflow.payrollsystem.migration;

import db.migration.V3__HashPlainPasswords;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

public class V3HashPlainPasswordsTest {

    @Test
    public void migrateHashesPlainPasswords() throws Exception {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:v3test;DB_CLOSE_DELAY=-1")) {
            try (Statement s = conn.createStatement()) {
                s.execute("CREATE TABLE users (id BIGINT AUTO_INCREMENT PRIMARY KEY, password VARCHAR(255))");
                s.execute("INSERT INTO users (password) VALUES ('password')");
            }

            V3__HashPlainPasswords migration = new V3__HashPlainPasswords();
            migration.migrate(new org.flywaydb.core.api.migration.Context() {
                @Override
                public Connection getConnection() {
                    return conn;
                }

                @Override
                public org.flywaydb.core.api.configuration.Configuration getConfiguration() {
                    return null; // not needed for this test
                }
            });

            try (PreparedStatement ps = conn.prepareStatement("SELECT password FROM users WHERE id = 1")) {
                try (ResultSet rs = ps.executeQuery()) {
                    assertThat(rs.next()).isTrue();
                    String pw = rs.getString(1);
                    assertThat(pw).startsWith("$2");
                }
            }
        }
    }
}
