package ru.otus.hw.http;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.exceptions.AuthenticationException;
import ru.otus.hw.exceptions.IdempotentRequestsException;
import ru.otus.hw.models.AccountBook;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final Map<UUID, Book> idempotencyKeyAddBook = new HashMap<>();

    private final Map<UUID, AccountBook> idempotencyKeyTakeBook = new HashMap<>();

    @Operation(summary = "Получить список всех книг")
    @GetMapping("api/book")
    public ResponseEntity<List<Book>> getAllBook() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll());
    }

    @Operation(summary = "Добавить книгу в библиотеку")
    @PostMapping("/api/book/creat")
    public ResponseEntity<Book> createBook(HttpServletRequest request, @RequestBody Book book) {

        var accountId = request.getHeader("X-Account-Id");
        var requestId = request.getHeader("X-Request-Id");

        if (accountId == null) {
            throw new AuthenticationException("Error authentication");
        }

        if (requestId == null) {
            throw new IdempotentRequestsException("Error idempotent requests");
        }

        var requestIdUUID = UUID.fromString(requestId);
        if (idempotencyKeyAddBook.containsKey(requestIdUUID)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(idempotencyKeyAddBook.get(requestIdUUID));
        }

        final Book newBook = bookService.create(book);
        idempotencyKeyAddBook.put(requestIdUUID, newBook);

        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    @Operation(summary = "Взять книгу в библиотеке")
    @PostMapping("/api/book/take/{bookId}")
    public ResponseEntity<AccountBook> takeBook(HttpServletRequest request, @PathVariable long bookId) {

        var accountId = request.getHeader("X-Account-Id");
        var requestId = request.getHeader("X-Request-Id");

        if (accountId == null) {
            throw new AuthenticationException("Error authentication");
        }

        if (requestId == null) {
            throw new IdempotentRequestsException("Error idempotent requests");
        }

        var requestIdUUID = UUID.fromString(requestId);
        if (idempotencyKeyTakeBook.containsKey(requestIdUUID)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(idempotencyKeyTakeBook.get(requestIdUUID));
        }

        final AccountBook accountBook = bookService.takeBook(bookId, Long.parseLong(accountId));
        idempotencyKeyTakeBook.put(requestIdUUID, accountBook);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountBook);
    }

    @Operation(summary = "Получить книгу по описанию")
    @GetMapping("api/book/find-by")
    public ResponseEntity<Book> getBook(@RequestParam String title, @RequestParam String author) {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookService.findByTitleAndAuthor(title, author));
    }
}
