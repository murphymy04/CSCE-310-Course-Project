package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.demo.models.Book;
import com.example.demo.services.BookService;
import com.example.demo.types.ApiResponse;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<Book>>> getBooks() {
        List<Book> results = bookService.getAllBooks();
        return ResponseEntity.ok(
            new ApiResponse<>(true, "All books found", results)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Book>>> search(@RequestParam String keyword) {
        List<Book> results = bookService.search(keyword);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Search successful.", results)
        );
    }
}
