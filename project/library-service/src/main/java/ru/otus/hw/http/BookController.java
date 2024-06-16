package ru.otus.hw.http;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AccountBookApiDto;
import ru.otus.hw.dto.BookApiDto;
import ru.otus.hw.dto.CreatBookApiDto;
import ru.otus.hw.exceptions.AuthenticationException;
import ru.otus.hw.exceptions.IdempotentRequestsException;
import ru.otus.hw.services.BookService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {

    private static final String HEADER_X_ACCOUNT_ID = "X-Account-Id";

    private static final String HEADER_X_REQUEST_ID = "X-Request-Id";

    private final BookService bookService;

    private final Map<UUID, BookApiDto> idempotencyKeyAddBook = new HashMap<>();

    private final Map<UUID, AccountBookApiDto> idempotencyKeyTakeBook = new HashMap<>();

    @Operation(summary = "Получить список всех книг")
    @GetMapping("api/book")
    public ResponseEntity<List<BookApiDto>> getAllBook() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll());
    }

    @Operation(summary = "Добавить книгу в библиотеку")
    @PostMapping("/api/book/creat")
    public ResponseEntity<BookApiDto> createBook(HttpServletRequest request, @RequestBody CreatBookApiDto book) {

        var accountId = request.getHeader(HEADER_X_ACCOUNT_ID);
        var requestId = request.getHeader(HEADER_X_REQUEST_ID);

        if (accountId == null) {
            throw new AuthenticationException("Error authentication");
        }

        if (requestId == null) {
            throw new IdempotentRequestsException("Error idempotent requests");
        }

        var requestIdUUID = UUID.fromString(requestId);
        if (idempotencyKeyAddBook.containsKey(requestIdUUID)) {
            log.info("Запрос будет возвращаться из кэша, requestI=[{}]", requestId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(idempotencyKeyAddBook.get(requestIdUUID));
        }

        final BookApiDto dto = bookService.create(book);
        idempotencyKeyAddBook.put(requestIdUUID, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Взять книгу в библиотеке")
    @PostMapping("/api/book/take/{bookId}")
    public ResponseEntity<AccountBookApiDto> takeBook(HttpServletRequest request, @PathVariable long bookId) {

        var accountId = request.getHeader(HEADER_X_ACCOUNT_ID);
        var requestId = request.getHeader(HEADER_X_REQUEST_ID);

        if (accountId == null) {
            throw new AuthenticationException("Error authentication");
        }

        if (requestId == null) {
            throw new IdempotentRequestsException("Error idempotent requests");
        }

        var requestIdUUID = UUID.fromString(requestId);
        if (idempotencyKeyTakeBook.containsKey(requestIdUUID)) {
            log.info("Запрос будет возвращаться из кэша, requestI=[{}]", requestId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(idempotencyKeyTakeBook.get(requestIdUUID));
        }

        final AccountBookApiDto accountBook = bookService.takeBook(bookId, Long.parseLong(accountId));
        idempotencyKeyTakeBook.put(requestIdUUID, accountBook);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountBook);
    }

    @Operation(summary = "Получить книгу по описанию")
    @GetMapping("api/book/find-by")
    public ResponseEntity<BookApiDto> getBook(@RequestParam String title, @RequestParam String author) {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookService.findByTitleAndAuthor(title, author));
    }
}
