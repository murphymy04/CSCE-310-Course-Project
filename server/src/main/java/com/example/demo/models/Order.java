package com.example.demo.models;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    private boolean paid = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // foreign key column in "orders" table
    private User user;
}
