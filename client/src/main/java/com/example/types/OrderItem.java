package com.example.types;

import java.math.BigDecimal;

public class OrderItem {
    private String title;
    private String author;
    private String purchaseType;
    private BigDecimal price;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
