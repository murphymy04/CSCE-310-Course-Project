package com.example.ui;

import com.example.ApiClient;
import com.example.types.ApiResponse;
import com.example.types.AuthResponse;
import com.example.types.ManageOrder;
import com.example.types.OrderItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.controlsfx.control.table.TableRowExpanderColumn;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class ManagerOrdersController {

    @FXML private TableView<ManageOrder> ordersTable;

    @FXML private TableColumn<ManageOrder, Void> expanderCol;
    @FXML private TableColumn<ManageOrder, Number> orderIdColumn;
    @FXML private TableColumn<ManageOrder, String> usernameColumn;
    @FXML private TableColumn<ManageOrder, BigDecimal> totalPriceColumn;
    @FXML private TableColumn<ManageOrder, Boolean> paidColumn;

    private final Gson gson = new Gson();

    @FXML
    public void initialize() {

        // Create expander column
        TableRowExpanderColumn<ManageOrder> expander =
                new TableRowExpanderColumn<>(this::createExpandedContent);

        // Insert at column index 0
        ordersTable.getColumns().add(0, expander);

        // Normal columns
        orderIdColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getOrderId()));

        usernameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));

        totalPriceColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotalPrice()));

        paidColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleBooleanProperty(data.getValue().getPaid()));

        loadOrders();
    }

    private void loadOrders() {
        Task<String> task = ApiClient.get("http://localhost:8080/api/order/manager/fetch");

        task.setOnSucceeded(e -> {
            String body = task.getValue();

            Type type = new TypeToken<ApiResponse<List<ManageOrder>>>(){}.getType();
            ApiResponse<List<ManageOrder>> response = gson.fromJson(body, type);

            if (!response.isSuccess()) {
                showError(response.getMessage());
                return;
            }

            ordersTable.setItems(FXCollections.observableArrayList(response.getData()));
        });

        task.setOnFailed(e -> {
            showError(task.getMessage());
        });

        new Thread(task).start();
    }

    /**
     * This creates the expanded row UI
     */
    private VBox createExpandedContent(TableRowExpanderColumn.TableRowDataFeatures<ManageOrder> features) {

        ManageOrder order = features.getValue();

        VBox box = new VBox(5);
        box.setStyle("-fx-padding: 10;");

        for (OrderItem item : order.getOrderItems()) {

            HBox row = new HBox(20);

            Label title = new Label(item.getTitle());
            Label author = new Label(item.getAuthor());
            Label price = new Label("$" + item.getPrice().toString());
            Label purchaseType = new Label(item.getPurchaseType());

            row.getChildren().addAll(title, author, price, purchaseType);
            box.getChildren().add(row);
        }

        return box;
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).show();
    }
}
