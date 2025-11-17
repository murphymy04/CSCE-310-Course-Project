package com.example.types;

public class LoginRequest {
    private String username;
    private String passwordHash;

    public LoginRequest(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
}

