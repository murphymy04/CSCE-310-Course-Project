package com.example.demo.models;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private BigDecimal total_price;
    private boolean paid;

    @ManyToOne
    @JoinColumn(name = "user_id")  // foreign key column in "orders" table
    private User user;
}
