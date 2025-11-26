package com.example.ui;

import com.example.ApiClient;
import com.example.CartService;
import com.example.types.ApiResponse;
import com.example.types.CartItem;
import com.example.types.OrderResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.lang.reflect.Type;
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
    private Runnable onSaveCallback;

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

        Task<String> task = ApiClient.postJson(
                "http://localhost:8080/api/order",
                json
        );

        task.setOnSucceeded(e -> {
            String body = task.getValue();

            Type type = new TypeToken<ApiResponse<OrderResponse>>(){}.getType();
            ApiResponse<OrderResponse> response = gson.fromJson(body, type);

            if (!response.isSuccess()) {
                showAlert("Order Failed", response.getMessage());
                return;
            }

            OrderResponse order = response.getData();

            StringBuilder bill = new StringBuilder();
            bill.append("ORDER RECEIPT\n");
            bill.append("----------------------------------------\n");
            bill.append("Order ID: ").append(order.getOrderId()).append("\n\n");

            bill.append("Items:\n");

            for (var item : order.getOrderItems()) {
                bill.append(" - ").append(item.getTitle())
                    .append(" by ").append(item.getAuthor())
                    .append("\n   Type: ").append(item.getPurchaseType())
                    .append("\n   Price: $").append(item.getPrice().setScale(2))
                    .append("\n\n");
            }

            bill.append("----------------------------------------\n");
            bill.append("Total: $").append(order.getTotalPrice().setScale(2)).append("\n");

            showBillWindow(bill.toString());

            CartService.clear();
            refreshTable();
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
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

    private void showBillWindow(String billText) {
        TextArea area = new TextArea(billText);
        area.setEditable(false);
        area.setWrapText(true);

        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Order Receipt");
        dialog.setHeaderText("Order Successful! Please check your email for a detailed receipt");
        dialog.getDialogPane().setContent(area);
        dialog.setResizable(true);
        dialog.showAndWait();
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }
}
