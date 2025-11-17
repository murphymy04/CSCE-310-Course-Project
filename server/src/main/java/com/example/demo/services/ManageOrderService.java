package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.example.demo.models.Order;
import com.example.demo.models.OrderBook;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.OrderBookRepository;
import com.example.demo.types.ManageOrder;
import com.example.demo.types.OrderItem;
import com.example.demo.types.PurchaseType;
import com.example.demo.types.OrderUpdateRequest;

@Service
public class ManageOrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderBookRepository orderBookRepository;

    public List<ManageOrder> getOrders() {
        List<Order> orders = orderRepository.findAll();
        List<ManageOrder> manageOrders = new ArrayList<>(orders.size());

        for (Order order : orders) {
            List<OrderBook> orderBooks = orderBookRepository.findByOrderId(order.getId());
            List<OrderItem> orderItems = new ArrayList<>(orderBooks.size());
            for (OrderBook orderBook : orderBooks) {
                BigDecimal price = orderBook.getPurchaseOrRental().equals(PurchaseType.purchase)
                        ? orderBook.getBook().getBuyPrice()
                        : orderBook.getBook().getRentPrice();

                orderItems.add(new OrderItem(
                        orderBook.getBook().getTitle(),
                        orderBook.getBook().getAuthor(),
                        orderBook.getPurchaseOrRental(),
                        price
                ));
            }
            manageOrders.add( new ManageOrder(
                order.getId(), order.getTotalPrice(), orderItems, order.isPaid(), order.getUser().getUsername()));
        }
        return manageOrders;
    }

    public void updateOrders(List<OrderUpdateRequest> orders) {
        for (OrderUpdateRequest order : orders) {
            Order newOrder = orderRepository.findById(order.getOrderId())
                        .orElseThrow(() -> new RuntimeException("Order with ID " + order.getOrderId() + " not found"));
            newOrder.setPaid(order.isPaid());
            orderRepository.save(newOrder);
        }
    }
}
