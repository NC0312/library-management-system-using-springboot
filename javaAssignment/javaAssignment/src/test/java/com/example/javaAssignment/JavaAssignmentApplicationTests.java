package com.example.javaAssignment;

import com.example.javaAssignment.models.Book;
import com.example.javaAssignment.models.User;
import com.example.javaAssignment.services.BookService;
import com.example.javaAssignment.services.UserService;
import com.example.javaAssignment.repositories.BookRepository;
import com.example.javaAssignment.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JavaAssignmentApplicationTests {

	@Mock
	private UserRepository userRepository;

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private UserService userService;

	@InjectMocks
	private BookService bookService;

	private User testUser;
	private Book testBook;

	@BeforeEach
	void setUp() {
		testUser = new User(1L, "Test User", "test@example.com");
		testBook = new Book();
		testBook.setId(1L);
		testBook.setTitle("Test Book");
		testBook.setAuthor("Test Author");
		testBook.setAvailable(true);
	}

	@Test
	void testCreateUser() {
		when(userRepository.save(any(User.class))).thenReturn(testUser);

		User savedUser = userService.saveUser(testUser);

		assertNotNull(savedUser);
		assertEquals("Test User", savedUser.getName());
		assertEquals("test@example.com", savedUser.getEmail());
	}

	@Test
	void testGetAllUsers() {
		when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

		List<User> users = userService.getAllUsers();

		assertFalse(users.isEmpty());
		assertEquals(1, users.size());
		assertEquals("Test User", users.get(0).getName());
	}

	@Test
	void testAddBook() {
		when(bookRepository.save(any(Book.class))).thenReturn(testBook);

		Book savedBook = bookService.saveBook(testBook);

		assertNotNull(savedBook);
		assertEquals("Test Book", savedBook.getTitle());
		assertEquals("Test Author", savedBook.getAuthor());
	}

	@Test
	void testGetAllBooks() {
		when(bookRepository.findAll()).thenReturn(Arrays.asList(testBook));

		List<Book> books = bookService.getAllBooks();

		assertFalse(books.isEmpty());
		assertEquals(1, books.size());
		assertEquals("Test Book", books.get(0).getTitle());
	}

	@Test
	void testBorrowBook() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
		when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

		User updatedUser = userService.borrowBook(1L, 1L);

		assertNotNull(updatedUser);
		assertFalse(updatedUser.getBorrowedBooks().isEmpty());
		assertTrue(updatedUser.getBorrowedBooks().contains(testBook));
		assertFalse(testBook.isAvailable());
	}

	@Test
	void testReturnBook() {
		testUser.getBorrowedBooks().add(testBook);
		testBook.setAvailable(false);

		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
		when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

		User updatedUser = userService.returnBook(1L, 1L);

		assertNotNull(updatedUser);
		assertTrue(updatedUser.getBorrowedBooks().isEmpty());
		assertTrue(testBook.isAvailable());
	}

	@Test
	void testBorrowUnavailableBook() {
		testBook.setAvailable(false);
		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
		when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

		assertThrows(RuntimeException.class, () -> userService.borrowBook(1L, 1L));
	}

	@Test
	void testReturnNotBorrowedBook() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
		when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

		assertThrows(RuntimeException.class, () -> userService.returnBook(1L, 1L));
	}
}