package com.example.ui;

import java.math.BigDecimal;

import com.example.ApiClient;
import com.example.types.Book;
import com.google.gson.Gson;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ManagerBookModalController {

    @FXML private Label titleLabel;
    @FXML private Label authorLabel;
    @FXML private TextField rentPriceField;
    @FXML private TextField buyPriceField;
    @FXML private CheckBox rentedCheckBox;
    
    private Book book;
    private Runnable onSaveCallback;
    private final Gson gson = new Gson();

    public void setBook(Book book) {
        this.book = book;

        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        rentPriceField.setText(book.getRentPrice().toString());
        buyPriceField.setText(book.getBuyPrice().toString());
        rentedCheckBox.setSelected(book.isRented());
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void onSave() {
        BigDecimal rentPrice = new BigDecimal(rentPriceField.getText().trim());
        BigDecimal buyPrice = new BigDecimal(buyPriceField.getText().trim());

        book.setRentPrice(rentPrice);
        book.setBuyPrice(buyPrice);
        book.setIsRented(rentedCheckBox.isSelected());

        String json = gson.toJson(book);

        Task<String> task = ApiClient.postJson(
            "http://localhost:8080/api/books/manager/update",
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

    @FXML
    private void handleDelete() {
        String json = gson.toJson(book);

        Task<String> task = ApiClient.deleteJson(
            "http://localhost:8080/api/books/manager/delete",
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

    private void closeWindow() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}
