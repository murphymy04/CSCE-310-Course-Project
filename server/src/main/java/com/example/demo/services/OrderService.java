package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.ArrayList;
import com.example.demo.models.*;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.OrderBookRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.types.BookOrderResponse;
import com.example.demo.types.OrderItem;
import com.example.demo.types.PurchaseType;

@Service
public class OrderService {
    @Autowired
    private BookService bookService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderBookRepository orderBookRepository;

    @Autowired
    private BookRepository bookRepository;

    public BookOrderResponse placeOrder(Map<Integer, Integer> orders, User user) {
        Order order = new Order();
        order.setUser(user);
        List<Book> books = new ArrayList<>(orders.size());
        List<OrderItem> orderItems = new ArrayList<>(orders.size());
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> entry : orders.entrySet()) {
            int bookId = entry.getKey();
            int orderType = entry.getValue();
            PurchaseType orderTypeEnum;
            BigDecimal price;
            Book book = bookService.searchById(bookId);

            if (orderType == 0) { // buy
                price = book.getBuyPrice();
                orderTypeEnum = PurchaseType.purchase;
            }
            else { // rent
                price = book.getRentPrice();
                book.setRented(true);
                orderTypeEnum = PurchaseType.rental;
            }

            book = bookRepository.save(book);
            books.add(book);

            totalPrice = totalPrice.add(price);
            orderItems.add(new OrderItem(book.getTitle(), book.getAuthor(), orderTypeEnum, price));
        }
        order.setTotalPrice(totalPrice);
        order = orderRepository.save(order);
        buildAndSaveOrderBooks(books, orderItems, order);
        BookOrderResponse bookOrder = new BookOrderResponse(order.getId(), totalPrice, orderItems);
        emailService.sendHtmlEmailAsync(user.getEmail(), "Order #" + order.getId() + " Confirmation", bookOrder);
        return bookOrder;
    }

    private List<OrderBook> buildAndSaveOrderBooks(List<Book> books, List<OrderItem> orderItems, Order order) {
        List<OrderBook> orderBooks = new ArrayList<>(books.size());
        for (int i=0; i<books.size(); i++) {
            OrderBook orderBook = new OrderBook();
            orderBook.setBook(books.get(i));
            orderBook.setOrder(order);
            orderBook.setPurchaseOrRental(orderItems.get(i).getPurchaseType());
            orderBooks.add(orderBookRepository.save(orderBook));
        }
        return orderBooks;
    }
}
