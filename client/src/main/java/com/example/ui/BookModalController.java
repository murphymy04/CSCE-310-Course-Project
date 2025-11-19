package com.example.ui;

import com.example.types.CartItem;
import com.example.types.Book;
import com.example.CartService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BookModalController {

    @FXML private Label titleLabel;
    @FXML private Label authorLabel;
    @FXML private Button rentButton;
    @FXML private Button buyButton;
    @FXML private Label rentPriceLabel;
    @FXML private Label buyPriceLabel;
    @FXML private Label rentedStatusLabel;

    private Book book;

    public void setBook(Book book) {
        this.book = book;

        titleLabel.setText(book.getTitle());
        authorLabel.setText("by " + book.getAuthor());
        rentPriceLabel.setText("Rent Price: $" + book.getRentPrice());
        buyPriceLabel.setText("Buy Price: $" + book.getBuyPrice());
        rentedStatusLabel.setText("Available to Rent");

        if (book.isRented()) {
            rentButton.setDisable(true);
            rentedStatusLabel.setText("Already Rented");
        }

        rentButton.setOnAction(e -> addToCart("rent"));
        buyButton.setOnAction(e -> addToCart("buy"));
    }

    private void addToCart(String type) {
        CartService.add(
            new CartItem(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                type.equals("rent"),
                type.equals("rent")
                    ? "$" + book.getRentPrice()
                    : "$" + book.getBuyPrice()
            )
        );

        closeModal();
    }

    @FXML
    private void closeModal() {
        ((Stage) titleLabel.getScene().getWindow()).close();
    }
}
