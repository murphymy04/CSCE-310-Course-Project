package com.example;

import javafx.concurrent.Task;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    private static final HttpClient http = HttpClient.newHttpClient();

    public static Task<String> postJson(String url, String jsonBody) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {

                HttpRequest.Builder builder = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json");

                if (UserSession.token != null && !UserSession.token.isBlank()) {
                    builder.header("Authorization", "Bearer " + UserSession.token);
                }

                HttpRequest request = builder
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                        .build();

                HttpResponse<String> response =
                        http.send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();
            }
        };
    }

    public static Task<String> get(String url) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {

                HttpRequest.Builder builder = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET();

                if (UserSession.token != null && !UserSession.token.isBlank()) {
                    builder.header("Authorization", "Bearer " + UserSession.token);
                }

                HttpRequest request = builder.build();

                HttpResponse<String> response =
                        http.send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();
            }
        };
    }
}
