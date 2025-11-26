package com.example.ui;

import java.math.BigDecimal;

import com.example.ApiClient;
import com.example.types.Book;
import com.google.gson.Gson;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ManagerAddBookModalController {
    
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField rentPriceField;
    @FXML private TextField buyPriceField;

    private Book book = new Book();
    private Runnable onSaveCallback;
    private final Gson gson = new Gson();

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void onSave() {
        BigDecimal rentPrice = new BigDecimal(rentPriceField.getText().trim());
        BigDecimal buyPrice = new BigDecimal(buyPriceField.getText().trim());

        book.setTitle(titleField.getText().trim());
        book.setAuthor(authorField.getText().trim());
        book.setRentPrice(rentPrice);
        book.setBuyPrice(buyPrice);
        book.setIsRented(false);

        String json = gson.toJson(book);

        Task<String> task = ApiClient.postJson(
            "http://localhost:8080/api/books/manager/add",
            json
        );

        task.setOnSucceeded(e -> {
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            closeWindow();
        });

        task.setOnFailed(e -> {
            System.out.println(task.getException());
        });

        new Thread(task).start();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}
