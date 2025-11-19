package com.example.types;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponse {
    private String orderId;
    private BigDecimal totalPrice;
    private List<OrderItem> orderItems;

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
