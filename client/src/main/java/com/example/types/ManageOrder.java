package com.example.types;

import java.math.BigDecimal;
import java.util.List;

public class ManageOrder extends BookOrderResponse {
    private boolean paid;
    private String username;

    public ManageOrder(int orderId, BigDecimal totalPrice, List<OrderItem> orderItems, boolean paid, String username) {
        super(orderId, totalPrice, orderItems);
        this.paid = paid;
        this.username = username;
    }

    public boolean getPaid() {
        return paid;
    }

    public String getUsername() {
        return username;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
