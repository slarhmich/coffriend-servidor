package com.brewingcode.coffriend_servidor.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("SystemController Integration Tests")
class SystemControllerIntegrationTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    @DisplayName("resetAll keeps admin account and admin can login again")
    void resetAllKeepsAdminAndAllowsRelogin() throws Exception {
        String adminEmail = "admin@example.com";
        String adminPassword = "1234";
  String baseUrl = "http://localhost:" + port;

  String createAdminPayload = """
    {
      "nom": "admin",
      "email": "%s",
      "password": "%s"
    }
    """.formatted(adminEmail, adminPassword);

  HttpResponse<String> createUserResponse = sendJsonRequest(
    "POST",
    baseUrl + "/api/usuaris",
    createAdminPayload,
    null
  );

  assertThat(createUserResponse.statusCode()).isEqualTo(201);
  JsonNode createdUserJson = objectMapper.readTree(createUserResponse.body());
  assertThat(createdUserJson.get("rol").asText()).isEqualTo("admin");

  String initialAdminToken = loginAndExtractToken(baseUrl, adminEmail, adminPassword);

  HttpResponse<String> resetResponse = sendJsonRequest(
    "DELETE",
    baseUrl + "/api/system/resetAll",
    null,
    initialAdminToken
  );

  assertThat(resetResponse.statusCode()).isEqualTo(200);

  String secondLoginToken = loginAndExtractToken(baseUrl, adminEmail, adminPassword);
    assertThat(secondLoginToken).isNotBlank();

    HttpResponse<String> usersResponse = sendJsonRequest(
        "GET",
        baseUrl + "/api/usuaris",
        null,
        secondLoginToken
    );

    assertThat(usersResponse.statusCode()).isEqualTo(200);
    JsonNode usersJson = objectMapper.readTree(usersResponse.body());
    assertThat(usersJson.isArray()).isTrue();
    assertThat(usersJson).isNotEmpty();

    boolean adminEmailPresent = false;
    for (JsonNode user : usersJson) {
      assertThat(user.get("rol").asText()).isEqualTo("admin");
      if (adminEmail.equals(user.get("email").asText())) {
        adminEmailPresent = true;
      }
    }

    assertThat(adminEmailPresent).isTrue();
    }

    private String loginAndExtractToken(String baseUrl, String email, String password) throws Exception {
  String loginPayload = """
    {
      "email": "%s",
      "password": "%s"
    }
    """.formatted(email, password);

  HttpResponse<String> loginResponse = sendJsonRequest(
    "POST",
    baseUrl + "/api/auth/login",
    loginPayload,
    null
  );

  assertThat(loginResponse.statusCode()).isEqualTo(200);

  JsonNode loginJson = objectMapper.readTree(loginResponse.body());
  assertThat(loginJson.get("token").asText()).isNotBlank();
  assertThat(loginJson.get("user").get("email").asText()).isEqualTo(email);

  return loginJson.get("token").asText();
    }

    private HttpResponse<String> sendJsonRequest(String method, String url, String body, String bearerToken) throws Exception {
  HttpRequest.BodyPublisher publisher = body != null
    ? HttpRequest.BodyPublishers.ofString(body)
    : HttpRequest.BodyPublishers.noBody();

  HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
    .uri(URI.create(url))
    .header("Content-Type", "application/json")
    .method(method, publisher);

  if (bearerToken != null && !bearerToken.isBlank()) {
      requestBuilder.header("Authorization", "Bearer " + bearerToken);
  }

  return httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }
}
