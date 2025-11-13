package com.example.demo.models;

import com.example.demo.types.PurchaseType;
import jakarta.persistence.*;

@Entity
@Table(name = "order_books")
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

    // --- Getters ---
    public int getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Book getBook() {
        return book;
    }

    public PurchaseType getPurchaseOrRental() {
        return purchaseOrRental;
    }

    // --- Setters ---
    public void setId(int id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setPurchaseOrRental(PurchaseType purchaseOrRental) {
        this.purchaseOrRental = purchaseOrRental;
    }
}
