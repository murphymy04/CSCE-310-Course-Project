package com.example.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    private final Gson gson = new Gson();

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
                errorLabel.setText(response.getMessage());
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
                stage.setScene(new Scene(new UserDashboard()));
            }
        } catch (Exception ex) {
            errorLabel.setText("Navigation error");
        }
    }

    @FXML
    private void goToRegister() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/ui/register.fxml")
            );

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            errorLabel.setText("Could not load register page.");
        }
    }
}

