package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load login page first
        Parent root = loadFXML("login.fxml");
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Loads an FXML file from src/main/resources/com/example/ui/
     */
    public static Parent loadFXML(String fileName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/com/example/ui/" + fileName)
            );
            return loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load FXML: " + fileName);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
