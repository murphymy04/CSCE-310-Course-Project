package com.example;

import com.example.types.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CartService {

    private static final List<CartItem> cart = new ArrayList<>();

    public static void add(CartItem item) {
        cart.add(item);
    }

    public static List<CartItem> getItems() {
        return cart;
    }

    public static void clear() {
        cart.clear();
    }

    public static double getTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart) {
            BigDecimal price = BigDecimal.valueOf(item.getPriceValue());
            total = total.add(price);
        }

        total = total.setScale(2, RoundingMode.HALF_UP);

        return total.doubleValue();
    }

    public static Map<String, Integer> getOrderRequest() {
        Map<String, Integer> orders = new HashMap<>();
        for (CartItem item : cart) {
            orders.put(String.valueOf(item.getBookId()), item.isRent() ? 1 : 0);
        }
        return orders;
    }
}
