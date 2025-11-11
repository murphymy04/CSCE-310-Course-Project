package com.example.demo.controllers;

import com.example.demo.types.AuthResponse;
import com.example.demo.models.User;
import com.example.demo.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody User loginRequest) {
        String token = authService.loginCustomer(
                loginRequest.getUsername(),
                loginRequest.getPasswordHash()
        );
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
