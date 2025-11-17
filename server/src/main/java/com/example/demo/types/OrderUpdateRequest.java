package com.example.demo.types;

public class OrderUpdateRequest {
    private int orderId;
    private boolean paid;

    public int getOrderId() {
        return orderId;
    }

    public boolean isPaid() {
        return paid;
    }
}
