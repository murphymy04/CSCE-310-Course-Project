package com.example.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

public class UserDashboard extends StackPane {
    public UserDashboard() {
        getChildren().add(new Label("Welcome User!"));
    }
}

