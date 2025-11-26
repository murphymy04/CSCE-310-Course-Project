package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.ManageBookService;
import com.example.demo.types.ApiResponse;
import com.example.demo.models.Book;

@RestController
@RequestMapping("/api/books/manager")
public class ManageBookController {
    @Autowired
    private ManageBookService manageBookService;

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<?>> getBooks() {
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Books fetched successfully.", manageBookService.getBooks())
        );
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> addBook(@RequestBody Book book) {
        try {
            return ResponseEntity.ok(
                new ApiResponse<>(true, book.getTitle() + " added successfully.", manageBookService.addBook(book))
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, e.getMessage() + "\n An unexpected error occurred while creating this book.", null));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateBook(@RequestBody Book book) {
        try {
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Book updated successfully.", manageBookService.updateBook(book))
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, e.getMessage() + "\n An unexpected error occurred while updating this book.", null));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> deleteBook(@RequestBody Book book) {
        try {
            manageBookService.deleteBook(book);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Book deleted successfully.", null)
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    new ApiResponse<>(false, e.getMessage() + "\n An unexpected error occurred while updating this book.", null)
                );
        }
    }
}
