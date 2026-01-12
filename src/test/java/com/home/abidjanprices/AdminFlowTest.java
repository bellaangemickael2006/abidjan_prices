package com.home.abidjanprices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminFlowTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void adminCanCreateProduit() throws Exception{
        // Login as admin created by DataInitializer (admin@local / admin123)
        Map<String,String> cred = new HashMap<>();
        cred.put("email","admin@local");
        cred.put("motDePasse","admin123");

        ResponseEntity<String> res = restTemplate.postForEntity("/api/auth/login", cred, String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode body = mapper.readTree(res.getBody());
        String token = body.get("token").asText();
        assertThat(token).isNotBlank();

        // Create product via admin endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String,String> prod = new HashMap<>();
        prod.put("nom","Tomate");
        prod.put("categorie","Légumes");
        prod.put("unite","kg");

        HttpEntity<Map<String,String>> entity = new HttpEntity<>(prod, headers);
        ResponseEntity<String> createRes = restTemplate.exchange("/api/admin/produits", HttpMethod.POST, entity, String.class);
        assertThat(createRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode created = mapper.readTree(createRes.getBody());
        assertThat(created.get("nom").asText()).isEqualTo("Tomate");
        assertThat(created.get("id").asLong()).isGreaterThan(0);
    }
}
