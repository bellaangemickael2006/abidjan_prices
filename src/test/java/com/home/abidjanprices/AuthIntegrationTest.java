package com.home.abidjanprices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void registerAndLogin() throws Exception{
        Map<String,Object> user = new HashMap<>();
        user.put("nom","Test User");
        user.put("email","test@example.com");
        user.put("motDePasse","password123");

        ResponseEntity<String> reg = restTemplate.postForEntity("/api/auth/register", user, String.class);
        assertThat(reg.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode body = mapper.readTree(reg.getBody());
        assertThat(body.has("token")).isTrue();

        Map<String,String> login = new HashMap<>();
        login.put("email","test@example.com");
        login.put("motDePasse","password123");
        ResponseEntity<String> log = restTemplate.postForEntity("/api/auth/login", login, String.class);
        assertThat(log.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode lb = mapper.readTree(log.getBody());
        assertThat(lb.has("token")).isTrue();

        String token = lb.get("token").asText();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        // access protected admin endpoint should be forbidden (user role)
        ResponseEntity<String> adminRes = restTemplate.postForEntity("/api/admin/produits", new HashMap<>(), String.class);
        // unauthenticated call expected 403 or 401; here it's unauthenticated because missing token
        assertThat(adminRes.getStatusCode().is4xxClientError()).isTrue();
    }
}
