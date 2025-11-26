package com.example.ui;

import com.example.ApiClient;
import com.example.MainApp;
import com.example.UserSession;
import com.example.types.ApiResponse;
import com.example.types.ManageOrder;
import com.example.types.OrderItem;
import com.example.types.OrderUpdateRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.controlsfx.control.table.TableRowExpanderColumn;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerOrdersController {

    @FXML private TableView<ManageOrder> ordersTable;
    @FXML private Button logoutButton;
    @FXML private Button paidButton;
    @FXML private Button bookButton;

    @FXML private TableColumn<ManageOrder, Number> orderIdColumn;
    @FXML private TableColumn<ManageOrder, String> usernameColumn;
    @FXML private TableColumn<ManageOrder, BigDecimal> totalPriceColumn;
    @FXML private TableColumn<ManageOrder, Boolean> paidColumn;

    private final Gson gson = new Gson();

    @FXML
    public void initialize() {
        ordersTable.getColumns().clear();

        // Create expander
        TableRowExpanderColumn<ManageOrder> expander =
                new TableRowExpanderColumn<>(this::createExpandedContent);
        ordersTable.getColumns().add(expander);
        ordersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Order ID column
        orderIdColumn = new TableColumn<>("Order ID");
        orderIdColumn.setPrefWidth(80);
        orderIdColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getOrderId()));

        // Username column
        usernameColumn = new TableColumn<>("Username");
        usernameColumn.setPrefWidth(140);
        usernameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getUsername()));

        // Price column
        totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setPrefWidth(120);
        totalPriceColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getTotalPrice()));

        // Paid column
        paidColumn = new TableColumn<>("Paid");
        paidColumn.setPrefWidth(80);
        paidColumn.setCellValueFactory(data ->
                new SimpleBooleanProperty(data.getValue().getPaid()).asObject());

        // Add remaining columns
        ordersTable.getColumns().addAll(orderIdColumn, usernameColumn, totalPriceColumn, paidColumn);
            
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

    @FXML
    private void markSelectedPaid() {
        List<ManageOrder> selected = ordersTable.getSelectionModel().getSelectedItems();

        if (selected.isEmpty()) {
            showError("Please select at least one unpaid order.");
            return;
        }

        // Filter only unpaid orders
        List<Integer> unpaidOrderIds = selected.stream()
                .filter(o -> !o.getPaid())
                .map(ManageOrder::getOrderId)
                .collect(Collectors.toList());

        if (unpaidOrderIds.isEmpty()) {
            showError("All selected orders are already paid.");
            return;
        }

        List<OrderUpdateRequest> body = new ArrayList<>(unpaidOrderIds.size());
        for (int id : unpaidOrderIds) {
            body.add(new OrderUpdateRequest(id, true));
        }

        String json = gson.toJson(body);

        System.out.println("Sending to backend: " + json);

        Task<String> task = ApiClient.postJson(
                "http://localhost:8080/api/order/manager/update",
                json
        );

        task.setOnSucceeded(e -> {
            showError("Orders updated successfully!");
            loadOrders();
        });

        task.setOnFailed(e -> showError("Failed to update orders: " + task.getException()));

        new Thread(task).start();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).show();
    }

    @FXML
    private void redirectBookView() {
        try {
            Stage stage = (Stage) ordersTable.getScene().getWindow();
            stage.setScene(new Scene(MainApp.loadFXML("manager_book_view.fxml")));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout() {
        UserSession.token = null;
        UserSession.isManager = false;
        try {
            Stage stage = (Stage) ordersTable.getScene().getWindow();
            stage.setScene(new Scene(MainApp.loadFXML("login.fxml")));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
