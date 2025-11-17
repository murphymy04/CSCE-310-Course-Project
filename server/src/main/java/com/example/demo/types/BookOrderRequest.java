package com.example.demo.types;

import java.util.Map;

public class BookOrderRequest {
    private Map<Integer, Integer> orders; // <Book ID, Buy: 0 Rent: 1>

    public Map<Integer, Integer> getOrders() {
        return orders;
    }

    public void setOrders(Map<Integer, Integer> orders) {
        this.orders = orders;
    }
}
