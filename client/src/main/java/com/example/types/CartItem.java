package com.example.types;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CartItem {

    private final int bookId;
    private final boolean rent; // true = rent, false = buy

    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty type;
    private final StringProperty price;

    public CartItem(int bookId, String title, String author, boolean rent, String price) {
        this.bookId = bookId;
        this.rent = rent;

        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.type = new SimpleStringProperty(rent ? "RENT" : "BUY");
        this.price = new SimpleStringProperty(price);
    }

    public int getBookId() { return bookId; }
    public boolean isRent() { return rent; }

    public StringProperty titleProperty() { return title; }
    public StringProperty authorProperty() { return author; }
    public StringProperty typeProperty() { return type; }
    public StringProperty priceProperty() { return price; }

    public double getPriceValue() {
        return Double.parseDouble(price.get().replace("$", ""));
    }
}
