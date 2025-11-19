package com.example.types;

import java.math.BigDecimal;

public class Book {
    private int id;
    private String title;
    private String author;
    private BigDecimal rentPrice;
    private BigDecimal buyPrice;
    private boolean isRented;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public BigDecimal getRentPrice() {
        return rentPrice;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public boolean isRented() {
        return isRented;
    }
}
