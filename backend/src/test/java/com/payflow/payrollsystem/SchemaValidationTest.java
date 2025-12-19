package com.payflow.payrollsystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SchemaValidationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void allEntityTablesExist() {
        String[] tables = new String[]{
                "companies",
                "users",
                "employees",
                "salaries",
                "payslips",
                "company_billing",
                "billing_plans",
                "taxes",
                "audit_logs"
        };

        for (String t : tables) {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ?",
                    Integer.class, t.toUpperCase());
            assertThat(count).isNotNull().withFailMessage("Table %s should exist", t).isGreaterThanOrEqualTo(1);
        }
    }
}
