package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.http.*;
import java.net.URI;
import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Waiting for server...");
        Button button = new Button("Fetch from API");

        button.setOnAction(e -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/hello"))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                label.setText(response.body());
            } catch (IOException | InterruptedException ex) {
                label.setText("Error connecting to server");
            }
        });

        VBox root = new VBox(10, button, label);
        stage.setScene(new Scene(root, 300, 200));
        stage.setTitle("JavaFX + Spring Boot Demo");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
