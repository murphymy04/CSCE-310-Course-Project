package com.example.ui;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

import com.example.ApiClient;
import com.example.MainApp;
import com.example.UserSession;
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

public class ManagerBooksController {
    
    @FXML private TableView<Book> resultsTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, BigDecimal> rentPriceColumn;
    @FXML private TableColumn<Book, BigDecimal> buyPriceColumn;
    @FXML private TableColumn<Book, Boolean> rentedColumn;

    private final Gson gson = new Gson();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("author"));
        rentPriceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("rentPrice"));
        buyPriceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("buyPrice"));
        rentedColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("rented"));

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

        loadBooks();
    }

    private void loadBooks() {
        Task<String> task = ApiClient.get("http://localhost:8080/api/books/manager/fetch");

        task.setOnSucceeded(e -> {
            String body = task.getValue();

            Type type = new TypeToken<ApiResponse<List<Book>>>() {}.getType();
            ApiResponse<List<Book>> response = gson.fromJson(body, type);

            List<Book> books = response.getData();
            resultsTable.setItems(FXCollections.observableList(books));
        });

        task.setOnFailed(e -> {
            showAlert("Request error: " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    public void refreshBooks() {
        loadBooks();
    }

    private void openBookDetail(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/ui/manager_book_modal.fxml")
            );

            Parent root = loader.load();

            ManagerBookModalController controller = loader.getController();
            controller.setBook(book);
            controller.setOnSaveCallback(() -> refreshBooks());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Book");
            stage.showAndWait(); // modal
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Unable to open book details.");
        }
    }

    @FXML
    private void addBook() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/ui/manager_add_book_modal.fxml")
            );

            Parent root = loader.load();

            ManagerAddBookModalController controller = loader.getController();
            controller.setOnSaveCallback(() -> refreshBooks());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Book");
            stage.showAndWait(); // modal       
        }
        catch (Exception e) {
            e.printStackTrace();;
        }
    }

    @FXML
    private void redirectOrdersView() {
        try {
            Stage stage = (Stage) resultsTable.getScene().getWindow();
            stage.setScene(new Scene(MainApp.loadFXML("manager_order_view.fxml")));
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
            Stage stage = (Stage) resultsTable.getScene().getWindow();
            stage.setScene(new Scene(MainApp.loadFXML("login.fxml")));
        }
        catch (Exception e) {
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
