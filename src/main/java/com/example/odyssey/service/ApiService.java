package com.example.odyssey.service;

import com.example.odyssey.model.LoginRequest;
import com.example.odyssey.model.SignupRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ApiService {

    private static final String BASE_URL = "https://userly-api.onrender.com/api/v1";
    private static final String AUTH_URL = BASE_URL + "/auth/login";
    private static final String REGISTER_URL = BASE_URL + "/users";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public static CompletableFuture<Boolean> loginUser(String username, String password) {
        try {
            LoginRequest loginRequest = new LoginRequest(username, password);
            String requestBody = MAPPER.writeValueAsString(loginRequest);
            HttpRequest request = buildRequest(AUTH_URL, requestBody);

            return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> response.statusCode() == 200)
                    .exceptionally(e -> {
                        e.printStackTrace();
                        return false;
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompletableFuture<Boolean> registerUser(String username, String email, String password) {
        try {
            SignupRequest signupRequest = new SignupRequest(username, email, password);
            String requestBody = MAPPER.writeValueAsString(signupRequest);
            HttpRequest request = buildRequest(REGISTER_URL, requestBody);

            return HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> response.statusCode() == 201)
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return false;
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpRequest buildRequest(String url, String body) throws JsonProcessingException {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }
}