package com.example.types;

public class OrderUpdateRequest {
    private int orderId;
    private boolean paid;

    public OrderUpdateRequest(int orderId, boolean paid) {
        this.orderId = orderId;
        this.paid = paid;
    }

    public int getOrderId() {
        return orderId;
    }

    public boolean isPaid() {
        return paid;
    }
}
