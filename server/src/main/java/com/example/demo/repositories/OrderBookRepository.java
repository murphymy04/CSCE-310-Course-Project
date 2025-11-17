package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.demo.models.OrderBook;

public interface OrderBookRepository extends JpaRepository<OrderBook, Integer> {
    List<OrderBook> findByOrderId(int orderId);
}
