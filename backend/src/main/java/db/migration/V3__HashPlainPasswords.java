package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Flyway Java-based migration to hash plaintext user passwords.
 * It finds users whose password does not already appear to be bcrypt and replaces it with a bcrypt hash.
 */
public class V3__HashPlainPasswords extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try (Statement s = context.getConnection().createStatement()) {
            try (ResultSet rs = s.executeQuery("SELECT id, password FROM users WHERE password IS NOT NULL AND password NOT LIKE '$2%';")) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String pw = rs.getString("password");
                    if (pw == null || pw.trim().isEmpty()) continue;

                    String hashed = encoder.encode(pw);
                    try (PreparedStatement ps = context.getConnection().prepareStatement("UPDATE users SET password = ? WHERE id = ?")) {
                        ps.setString(1, hashed);
                        ps.setLong(2, id);
                        ps.executeUpdate();
                    }
                }
            }
        }
    }
}
