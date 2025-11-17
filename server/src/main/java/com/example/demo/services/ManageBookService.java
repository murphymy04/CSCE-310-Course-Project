package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.demo.models.Book;
import com.example.demo.repositories.BookRepository;

@Service
public class ManageBookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Book book) {
        bookRepository.findById(book.getId())
            .orElseThrow(() -> new RuntimeException("Book with ID " + book.getId() + " not found"));
        return bookRepository.save(book);
    }
    
    public void deleteBook(Book book) {
        bookRepository.findById(book.getId())
            .orElseThrow(() -> new RuntimeException("Book with ID " + book.getId() + " not found"));
        bookRepository.delete(book);
    }
}
