package com.payflow.payrollsystem.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void loginSuperadmin_returnsTokenWithRoleSuperadmin() throws Exception {
        String body = "{\"email\":\"super@test.com\",\"password\":\"password\"}";

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String resp = result.getResponse().getContentAsString();
        JsonNode node = mapper.readTree(resp);
        assertThat(node.has("token")).isTrue();

        String token = node.get("token").asText();
        String[] parts = token.split("\\.");
        assertThat(parts.length).isEqualTo(3);

        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
        JsonNode payload = mapper.readTree(payloadJson);

        // subject is the email
        assertThat(payload.get("sub").asText()).isEqualTo("super@test.com");
        // role should be lowercase 'superadmin' as per token generation
        assertThat(payload.get("role").asText()).isEqualTo("superadmin");
    }

    @Test
    public void loginInvalidCredentials_returnsUnauthorized() throws Exception {
        String body = "{\"email\":\"super@test.com\",\"password\":\"wrong\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }
}
