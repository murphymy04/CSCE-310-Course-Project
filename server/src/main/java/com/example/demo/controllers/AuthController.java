package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.JwtService;
import com.example.demo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> register(@RequestBody User user) {
        if (((userService.findByUsername(user.getUsername())) != null) || (userService.findByEmail(user.getEmail()) != null)) {
            return ResponseEntity.badRequest().body("Username or email already registered");
        }
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(Map.of("message", "User registered", "username", registeredUser.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User savedUser = userService.findByUsername(user.getUsername());
        if (user == null || !userService.checkPassword(user.getPasswordHash(), savedUser.getPasswordHash())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(
            Map.of(
                "token", token,
                "isManager", savedUser.isManager()
        ));
    }
}
