package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.ManageBookService;
import com.example.demo.models.Book;

@RestController
@RequestMapping("/api/books/manager")
public class ManageBookController {
    @Autowired
    private ManageBookService manageBookService;

    @GetMapping("/fetch")
    public ResponseEntity<?> getBooks() {
        return ResponseEntity.ok(manageBookService.getBooks());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        try {
            return ResponseEntity.ok(manageBookService.addBook(book));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage() + "\n An unexpected error occurred while creating this book.");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateBook(@RequestBody Book book) {
        try {
            return ResponseEntity.ok(manageBookService.updateBook(book));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage() + "\n An unexpected error occurred while updating this book.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBook(@RequestBody Book book) {
        try {
            manageBookService.deleteBook(book);
            return ResponseEntity.ok("Book deleted successfully.");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage() + "\n An unexpected error occurred while updating this book.");
        }
    }
}
