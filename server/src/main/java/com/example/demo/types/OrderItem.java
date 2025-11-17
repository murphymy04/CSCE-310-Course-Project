package com.example.demo.types;

import java.math.BigDecimal;

public class OrderItem {
    private String title;
    private String author;
    private PurchaseType purchaseType;
    private BigDecimal price;

    public OrderItem(String title, String author, PurchaseType purchaseType, BigDecimal price) {
        this.title = title;
        this.author = author;
        this.purchaseType = purchaseType;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    } 

    public BigDecimal getPrice() {
        return price;
    }
}
