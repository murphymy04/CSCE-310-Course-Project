package com.example.types;

import java.math.BigDecimal;
import java.util.List;

public class BookOrderResponse {
    private int orderId;
    private BigDecimal totalPrice;
    private List<OrderItem> orderItems;

    public BookOrderResponse(int orderId, BigDecimal totalPrice, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems;
    }

    public int getOrderId() {
        return orderId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
