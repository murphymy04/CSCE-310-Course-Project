package com.example.demo.models;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    private boolean paid = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // foreign key column in "orders" table
    private User user;

    // --- Getters ---
    public int getId() {
        return id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public boolean isPaid() {
        return paid;
    }

    public User getUser() {
        return user;
    }

    // --- Setters ---
    public void setId(int id) {
        this.id = id;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
