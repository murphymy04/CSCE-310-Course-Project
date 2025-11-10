package com.example.demo.models;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String author;
    private BigDecimal rent_price;
    private BigDecimal buy_price;
    private boolean isRented;
}
