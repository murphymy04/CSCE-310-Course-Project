package com.example.demo.models;

import com.example.demo.types.PurchaseType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Order_Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;

    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_or_rental", nullable = false)
    private PurchaseType purchaseOrRental;
}
