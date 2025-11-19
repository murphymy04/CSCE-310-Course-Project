package com.example.ui;

import com.example.ApiClient;
import com.example.CartService;
import com.example.types.CartItem;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartController {

    @FXML private TableView<CartItem> cartTable;
    @FXML private Label totalLabel;

    @FXML private TableColumn<CartItem, String> titleCol;
    @FXML private TableColumn<CartItem, String> authorCol;
    @FXML private TableColumn<CartItem, String> typeCol;
    @FXML private TableColumn<CartItem, String> priceCol;

    private final Gson gson = new Gson();

    @FXML
    public void initialize() {
        // Bind columns
        titleCol.setCellValueFactory(c -> c.getValue().titleProperty());
        authorCol.setCellValueFactory(c -> c.getValue().authorProperty());
        typeCol.setCellValueFactory(c -> c.getValue().typeProperty());
        priceCol.setCellValueFactory(c -> c.getValue().priceProperty());

        refreshTable();
    }

    private void refreshTable() {
        List<CartItem> items = CartService.getItems();
        cartTable.setItems(FXCollections.observableArrayList(items));
        totalLabel.setText("$" + CartService.getTotal());
    }

    @FXML
    private void clearCart() {
        CartService.clear();
        refreshTable();
    }

    @FXML
    private void placeOrder() {

        if (CartService.getItems().isEmpty()) {
            showAlert("Cart is empty", "Add items before placing an order.");
            return;
        }

        Map<String, Integer> orders = new HashMap<>();

        CartService.getOrderRequest().forEach((bookId, orderType) -> {
            orders.put(bookId.toString(), orderType);
        });

        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("orders", orders);

        String json = gson.toJson(wrapper);
        System.out.println("Outgoing order JSON: " + json);

        Task<String> task = ApiClient.postJson(
                "http://localhost:8080/api/order",
                json
        );

        task.setOnSucceeded(e -> {
            CartService.clear();
            refreshTable();
            showAlert("Success", "Order placed successfully!");
            closeWindow();
        });

        task.setOnFailed(e -> {
            showAlert("Error", "Could not place order.\n" + task.getException());
        });

        new Thread(task).start();
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) cartTable.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
