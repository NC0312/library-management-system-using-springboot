package com.example.javaAssignment.services;

import com.example.javaAssignment.models.Book;
import com.example.javaAssignment.models.User;
import com.example.javaAssignment.repositories.BookRepository;
import com.example.javaAssignment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public User saveUser(User user) {

        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
            return userRepository.findAll();


    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User borrowBook(Long userId, Long bookId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);

        if (userOpt.isPresent() && bookOpt.isPresent()) {
            User user = userOpt.get();
            Book book = bookOpt.get();

            if (book.isAvailable()) {
                user.getBorrowedBooks().add(book);
                book.setAvailable(false);
                userRepository.save(user);
                bookRepository.save(book);
            } else {
                throw new RuntimeException("Book is not available for borrowing.");
            }
        } else {
            throw new RuntimeException("User or Book not found.");
        }

        return userOpt.get();
    }

    public User returnBook(Long userId, Long bookId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);

        if (userOpt.isPresent() && bookOpt.isPresent()) {
            User user = userOpt.get();
            Book book = bookOpt.get();

            if (user.getBorrowedBooks().contains(book)) {
                user.getBorrowedBooks().remove(book);
                book.setAvailable(true);
                userRepository.save(user);
                bookRepository.save(book);
            } else {
                throw new RuntimeException("User did not borrow this book.");
            }
        } else {
            throw new RuntimeException("User or Book not found.");
        }

        return userOpt.get();
    }
}
