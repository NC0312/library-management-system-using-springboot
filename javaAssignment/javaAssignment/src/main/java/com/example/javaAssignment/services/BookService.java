package com.example.javaAssignment.services;

import com.example.javaAssignment.models.Book;
import com.example.javaAssignment.models.User;
import com.example.javaAssignment.repositories.BookRepository;
import com.example.javaAssignment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book borrowBook(Long bookId, Long userId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (bookOpt.isPresent() && userOpt.isPresent()) {
            Book book = bookOpt.get();
            User user = userOpt.get();

            if (book.isAvailable()) {
                book.setAvailable(false);
                user.getBorrowedBooks().add(book);
                bookRepository.save(book);
                userRepository.save(user);
                return book;
            } else {
                throw new RuntimeException("Book is not available for borrowing.");
            }
        } else {
            throw new RuntimeException("Book or User not found.");
        }
    }

    public Book returnBook(Long bookId, Long userId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (bookOpt.isPresent() && userOpt.isPresent()) {
            Book book = bookOpt.get();
            User user = userOpt.get();

            if (user.getBorrowedBooks().contains(book)) {
                user.getBorrowedBooks().remove(book);
                book.setAvailable(true);
                bookRepository.save(book);
                userRepository.save(user);
                return book;
            } else {
                throw new RuntimeException("User did not borrow this book.");
            }
        } else {
            throw new RuntimeException("Book or User not found.");
        }
    }
}
