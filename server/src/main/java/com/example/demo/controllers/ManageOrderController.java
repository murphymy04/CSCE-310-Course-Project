package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.demo.types.ManageOrder;
import com.example.demo.types.OrderUpdateRequest;
import com.example.demo.services.ManageOrderService;

@RestController
@RequestMapping("/api/order/manager")
public class ManageOrderController {
    @Autowired
    private ManageOrderService manageOrderService;
    
    @GetMapping("/fetch")
    public ResponseEntity<?> viewOrders() {
        try {
            List<ManageOrder> response = manageOrderService.getOrders();
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage() + "\n An unexpected error occurred while fetching the orders.");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePaymentStatus(@RequestBody List<OrderUpdateRequest> orders) {
        try {
            manageOrderService.updateOrders(orders);
            return ResponseEntity.ok("Updated status successfully");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage() + "\n An unexpected error occurred while updating the order.");
        }
    }
}
