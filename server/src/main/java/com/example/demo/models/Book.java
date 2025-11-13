package com.example.demo.models;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private BigDecimal rentPrice;

    @Column(nullable = false)
    private BigDecimal buyPrice;

    private boolean isRented = false;

    // --- Getters ---
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

    // --- Setters ---
    public void setId(int id) {
        this.id = id;
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

    public void setRented(boolean rented) {
        isRented = rented;
    }
}
