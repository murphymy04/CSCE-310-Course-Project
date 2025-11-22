package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.demo.types.ManageOrder;
import com.example.demo.types.OrderUpdateRequest;
import com.example.demo.services.ManageOrderService;
import com.example.demo.types.ApiResponse;

@RestController
@RequestMapping("/api/order/manager")
public class ManageOrderController {
    @Autowired
    private ManageOrderService manageOrderService;
    
    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<?>> viewOrders() {
        try {
            List<ManageOrder> response = manageOrderService.getOrders();
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Orders fetched successfully.", response)
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    new ApiResponse<>(false, e.getMessage() + "\n An unexpected error occurred while fetching the orders.", null)
                );
        }
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<?>> updatePaymentStatus(@RequestBody List<OrderUpdateRequest> orders) {
        try {
            manageOrderService.updateOrders(orders);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Updated status successfully", null)
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    new ApiResponse<>(false, e.getMessage() + "\n An unexpected error occurred while updating the order.", null)
                );
        }
    }
}
