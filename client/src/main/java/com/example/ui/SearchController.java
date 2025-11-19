package com.example.ui;

import com.example.ApiClient;
import com.example.types.ApiResponse;
import com.example.types.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class SearchController {

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private TableView<Book> resultsTable;

    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, BigDecimal> rentPriceColumn;
    @FXML private TableColumn<Book, BigDecimal> buyPriceColumn;
    @FXML private TableColumn<Book, Boolean> rentedColumn;

    private final Gson gson = new Gson();

    @FXML
    public void initialize() {
        // Bind columns to POJO getters (must match field names)
        titleColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("author"));
        rentPriceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("rentPrice"));
        buyPriceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("buyPrice"));
        rentedColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("rented"));

        searchButton.setOnAction(e -> searchBooks());
        resultsTable.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Book selected = row.getItem();
                    openBookDetail(selected);
                }
            });

            return row;
        });
    }

    private void searchBooks() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            showAlert("Please enter a search keyword.");
            return;
        }

        String url = "http://localhost:8080/api/books/search?keyword=" + keyword;

        Task<String> task = ApiClient.get(url);

        task.setOnSucceeded(e -> {
            String body = task.getValue();

            Type type = new TypeToken<ApiResponse<List<Book>>>() {}.getType();
            ApiResponse<List<Book>> response = gson.fromJson(body, type);

            if (!response.isSuccess()) {
                showAlert("Search failed: " + response.getMessage());
                return;
            }

            List<Book> books = response.getData();
            resultsTable.setItems(FXCollections.observableList(books));
        });

        task.setOnFailed(e -> {
            showAlert("Request error: " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    private void openBookDetail(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/ui/book_modal.fxml")
            );

            Parent root = loader.load();

            BookModalController controller = loader.getController();
            controller.setBook(book);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Book Options");
            stage.showAndWait(); // modal
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Unable to open book details.");
        }
    }

    @FXML
    private void openCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ui/cart-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Shopping Cart");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
