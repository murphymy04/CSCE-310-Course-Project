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
import com.example.demo.types.ApiResponse;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> orderBooks(@AuthenticationPrincipal User user, @RequestBody BookOrderRequest request) {
        try {
            Map<Integer, Integer> orders = request.getOrders();

            BookOrderResponse data = orderService.placeOrder(orders, user);

            return ResponseEntity.ok(
                new ApiResponse<>(true, "Order placed successfuly.", data)
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                        new ApiResponse<>(false, "An unexpected error occurred while placing the order.", null)
                    );
        }
    }
}
