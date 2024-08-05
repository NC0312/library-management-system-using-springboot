package com.example.javaAssignment.controllers;

import com.example.javaAssignment.models.User;
import com.example.javaAssignment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/borrow/{bookId}")
    public ResponseEntity<User> borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return ResponseEntity.ok(userService.borrowBook(userId, bookId));
    }

    @PostMapping("/{userId}/return/{bookId}")
    public ResponseEntity<User> returnBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return ResponseEntity.ok(userService.returnBook(userId, bookId));
    }
}
