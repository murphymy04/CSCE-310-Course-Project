package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.demo.models.Book;
import com.example.demo.repositories.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> search(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    public Book searchById(Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book with ID " + id + " not found"));
    }
}
