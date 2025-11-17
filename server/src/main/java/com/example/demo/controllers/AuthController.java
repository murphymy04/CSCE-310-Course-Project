package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.JwtService;
import com.example.demo.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.types.ApiResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody User user) {
        if (((userService.findByUsername(user.getUsername())) != null) || (userService.findByEmail(user.getEmail()) != null)) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, "Username or email already registered", null)
            );
        }
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Registration successful", registeredUser.getUsername())
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(401).body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody User user) {
        try {
            User savedUser = userService.findByUsername(user.getUsername());
            if (user == null || !userService.checkPassword(user.getPasswordHash(), savedUser.getPasswordHash())) {
                return ResponseEntity.status(401).body(
                    new ApiResponse<>(false, "Invalid credentials", null)
                );
            }
            String token = jwtService.generateToken(user.getUsername());

            Map<String, Object> data = Map.of(
                "token", token,
                "isManager", savedUser.isManager()
            );

            return ResponseEntity.ok(
                new ApiResponse<>(true, "Login successful", data)
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(401).body(
                new ApiResponse<>(false, e.getMessage(), null)
            );
        }

    }
}
