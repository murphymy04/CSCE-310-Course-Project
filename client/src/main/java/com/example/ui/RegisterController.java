package com.example.ui;

import com.example.ApiClient;
import com.example.types.RegisterRequest;
import com.google.gson.Gson;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox managerCheckBox;
    @FXML private Label errorLabel;

    private final Gson gson = new Gson();

    @FXML
    private void register() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        boolean isManager = managerCheckBox.isSelected();

        RegisterRequest request = new RegisterRequest(username, email, password, isManager);

        Task<String> task = ApiClient.postJson(
                "http://localhost:8080/api/auth/register",
                gson.toJson(request)
        );

        task.setOnSucceeded(e -> {
            String body = task.getValue();

            if (body == null) {
                errorLabel.setText("Registration failed.");
                return;
            }

            goToLogin();
        });

        task.setOnFailed(e -> {
            errorLabel.setText(task.getMessage());
        });

        new Thread(task).start();
    }

    @FXML
    private void goToLogin() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(com.example.MainApp.loadFXML("login.fxml")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
