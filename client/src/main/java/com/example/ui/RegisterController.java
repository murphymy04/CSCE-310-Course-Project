package com.example.ui;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.concurrent.Task;
import com.example.ApiClient;
import com.example.types.RegisterRequest;

public class RegisterController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nameField;
    @FXML private Label messageLabel;
    @FXML private Label errorLabel;

    private final Gson gson = new Gson();

    @FXML
    private void register() {
        String json = gson.toJson(new RegisterRequest(
                nameField.getText(),
                emailField.getText(),
                passwordField.getText()
        ));

        Task<String> task = ApiClient.postJson(
                "http://localhost:8080/api/auth/register",
                json
        );

        task.setOnSucceeded(e -> {
            messageLabel.setText("Registration successful! Return to login.");
        });

        task.setOnFailed(e -> {
            messageLabel.setText("Registration failed.");
        });

        new Thread(task).start();
    }

    @FXML
    private void goToLogin() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/ui/login.fxml")
            );

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            errorLabel.setText("Could not load login page.");
        }
    }
}

