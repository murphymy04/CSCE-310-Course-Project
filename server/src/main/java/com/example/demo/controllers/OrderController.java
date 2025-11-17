package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.Map;
import com.example.demo.models.User;
import com.example.demo.services.OrderService;
import com.example.demo.types.BookOrderRequest;
import com.example.demo.types.BookOrderResponse;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping()
    public ResponseEntity<?> orderBooks(@AuthenticationPrincipal User user, @RequestBody BookOrderRequest request) {
        try {
            Map<Integer, Integer> orders = request.getOrders();

            BookOrderResponse response = orderService.placeOrder(orders, user);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while placing the order.");
        }
    }
}
