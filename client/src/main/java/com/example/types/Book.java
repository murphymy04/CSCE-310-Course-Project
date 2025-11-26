package com.example.types;

import java.math.BigDecimal;

public class Book {
    private int id;
    private String title;
    private String author;
    private BigDecimal rentPrice;
    private BigDecimal buyPrice;
    private boolean rented;
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
        return rented;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setRentPrice(BigDecimal rentPrice) {
        this.rentPrice = rentPrice;
    } 

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setIsRented(boolean isRented) {
        this.rented = isRented;
        this.isRented = isRented;
    }
}
