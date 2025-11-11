package com.example.demo.models;

import com.example.demo.types.PurchaseType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_books")
@Data
public class OrderBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseType purchaseOrRental;
}
