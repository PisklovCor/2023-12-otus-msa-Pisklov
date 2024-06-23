package ru.otus.hw.http;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.AccountBookApiDto;
import ru.otus.hw.dto.AccountAllBookApiDto;
import ru.otus.hw.dto.BookApiDto;
import ru.otus.hw.dto.CreatBookApiDto;
import ru.otus.hw.dto.in.CreatBookApiOrderDto;
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

    private static final String HEADER_X_EMAIL = "X-Email";

    private final BookService bookService;

    private final Map<UUID, BookApiDto> idempotencyKeyAddBook = new HashMap<>();

    private final Map<UUID, AccountBookApiDto> idempotencyKeyTakeBook = new HashMap<>();

    private final Map<UUID, String> idempotencyKeyLeaveRequestForABook = new HashMap<>();

    @Operation(summary = "Получить список всех книг")
    @GetMapping("api/book")
    public ResponseEntity<List<BookApiDto>> getAllBook(HttpServletRequest request) {

        idempotencyAccountIdHelper(request);
        idempotencyRequestIdHelper(request);

        return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll());
    }

    @Operation(summary = "Получить все книги пользоваетля")
    @GetMapping("api/book/find-by-account")
    public ResponseEntity<AccountAllBookApiDto> getBook(HttpServletRequest request) {

        idempotencyRequestIdHelper(request);
        var accountId = idempotencyAccountIdHelper(request);

        return ResponseEntity.status(HttpStatus.OK).body(
                bookService.getBookByAccount(accountId));
    }

    @Operation(summary = "Добавить книгу в библиотеку")
    @PostMapping("/api/book/creat")
    public ResponseEntity<BookApiDto> createBook(HttpServletRequest request, @RequestBody CreatBookApiDto book) {

        var accountId = idempotencyAccountIdHelper(request);
        var requestIdUUID = idempotencyRequestIdHelper(request);
        var email = idempotencyEmailHelper(request);

        if (idempotencyKeyAddBook.containsKey(requestIdUUID)) {
            log.info("Запрос будет возвращаться из кэша, requestI=[{}]", requestIdUUID);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(idempotencyKeyAddBook.get(requestIdUUID));
        }

        final BookApiDto dto = bookService.create(book, accountId, email);
        idempotencyKeyAddBook.put(requestIdUUID, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Добавить книгу в библиотеку")
    @PostMapping("/api/internal/book/creat")
    public ResponseEntity<BookApiDto> createBookInternal(@RequestBody CreatBookApiOrderDto creatBookApiOrderDto) {

        final BookApiDto dto = bookService.createInternal(creatBookApiOrderDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Взять книгу в библиотеке")
    @PostMapping("/api/book/take/{bookId}")
    public ResponseEntity<AccountBookApiDto> takeBook(HttpServletRequest request, @PathVariable long bookId) {

        var accountId = idempotencyAccountIdHelper(request);
        var requestIdUUID = idempotencyRequestIdHelper(request);
        var email = idempotencyEmailHelper(request);

        if (idempotencyKeyTakeBook.containsKey(requestIdUUID)) {
            log.info("Запрос будет возвращаться из кэша, requestI=[{}]", requestIdUUID);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(idempotencyKeyTakeBook.get(requestIdUUID));
        }

        final AccountBookApiDto accountBook = bookService.takeBook(bookId, accountId, email);
        idempotencyKeyTakeBook.put(requestIdUUID, accountBook);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountBook);
    }

    @Operation(summary = "Оставить заявку на пополнение библиотеки")
    @PostMapping("api/book/leave-request")
    public ResponseEntity<String> leaveRequestForABook(HttpServletRequest request, @RequestBody CreatBookApiDto book) {

        var accountId = idempotencyAccountIdHelper(request);
        var requestIdUUID = idempotencyRequestIdHelper(request);
        var email = idempotencyEmailHelper(request);

        if (idempotencyKeyLeaveRequestForABook.containsKey(requestIdUUID)) {
            log.info("Запрос будет возвращаться из кэша, requestI=[{}]", requestIdUUID);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(idempotencyKeyLeaveRequestForABook.get(requestIdUUID));
        }

        idempotencyKeyLeaveRequestForABook.put(requestIdUUID, "Ваша книга будет доступна в библиотеке позже");
        bookService.leaveRequestForABook(book, accountId, email);

        return ResponseEntity.status(HttpStatus.CREATED).body("Запрос на вашу книгу будет обработан");
    }

    @Operation(summary = "Удалить книгу по ID")
    @DeleteMapping("/api/book/admin/{bookId}")
    public ResponseEntity<Void> deleteOrder(HttpServletRequest request, @PathVariable long bookId) {

        idempotencyAccountIdHelper(request);
        idempotencyRequestIdHelper(request);
        idempotencyEmailHelper(request);
        var userLogin = request.getHeader("X-User");

        if (userLogin == null) {
            throw new IdempotentRequestsException("Empty header");
        }

        if (!userLogin.equals("admin")) {
            throw new IdempotentRequestsException("Error user not ADMIN");
        }

        bookService.deleteBook(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private UUID idempotencyRequestIdHelper(HttpServletRequest request) {
        var requestId = request.getHeader(HEADER_X_REQUEST_ID);

        if (requestId == null) {
            throw new IdempotentRequestsException("Error idempotent requests");
        }
         return UUID.fromString(requestId);
    }

    private long idempotencyAccountIdHelper(HttpServletRequest request) {
        var accountId = request.getHeader(HEADER_X_ACCOUNT_ID);

        if (accountId == null) {
            throw new AuthenticationException("Error authentication");
        }
        return Long.parseLong(accountId);
    }

    private String idempotencyEmailHelper(HttpServletRequest request) {
        var email = request.getHeader(HEADER_X_EMAIL);

        if (email == null) {
            throw new AuthenticationException("Error get email");
        }
        return email;
    }
}
