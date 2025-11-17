package com.example.types;

public class RegisterRequest {
    private String username;
    private String email;
    private String passwordHash;
    private boolean manager;

    public RegisterRequest(String username, String email, String password, boolean manager) {
        this.username = username;
        this.email = email;
        this.passwordHash = password;
        this.manager = manager;
    }
}

