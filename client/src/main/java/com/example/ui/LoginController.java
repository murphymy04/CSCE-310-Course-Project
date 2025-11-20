package com.example.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import javafx.scene.Scene;
import com.example.ApiClient;
import com.example.UserSession;
import com.example.types.ApiResponse;
import com.example.types.AuthResponse;
import com.example.types.LoginRequest;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    private final Gson gson = new Gson();

    @FXML
    public void initialize() {
        loginButton.setDefaultButton(true);
    }

    @FXML
    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String json = gson.toJson(new LoginRequest(username, password));

        Task<String> task = ApiClient.postJson(
                "http://localhost:8080/api/auth/login", json
        );

        task.setOnSucceeded(e -> {
            String body = task.getValue();

            Type type = new TypeToken<ApiResponse<AuthResponse>>(){}.getType();
            ApiResponse<AuthResponse> response = gson.fromJson(body, type);

            if (!response.isSuccess()) {
                errorLabel.setText("Invalid username or password");
                return;
            }

            AuthResponse auth = response.getData();
            UserSession.token = auth.getToken();
            UserSession.isManager = auth.isManager();
            redirect(auth.isManager());
        });

        task.setOnFailed(e -> {
            errorLabel.setText(task.getMessage());
        });

        new Thread(task).start();
    }

    private void redirect(boolean isManager) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();

            if (isManager) {
                stage.setScene(new Scene(new ManagerDashboard()));
            } else {
                stage.setScene(new Scene(com.example.MainApp.loadFXML("search.fxml")));
            }
        } catch (Exception ex) {
            errorLabel.setText("Navigation error");
        }
    }

    @FXML
    private void goToRegister() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(com.example.MainApp.loadFXML("register.fxml")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
