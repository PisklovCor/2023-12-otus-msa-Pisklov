package ru.otus.hw.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.AuthenticationException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.IdempotentRequestsException;
import ru.otus.hw.exceptions.ExternalServiceInteractionException;
import ru.otus.hw.exceptions.AuthenticationAdminException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handler(Exception e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON).body("Ошибка сервера library-service");

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handler(EntityNotFoundException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON).body("Ошибка поиска library-service");

    }

    @ExceptionHandler(IdempotentRequestsException.class)
    public ResponseEntity<String> handler(IdempotentRequestsException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("Ошибка идемпотентности library-service");

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handler(AuthenticationException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
                .body("Ошибка аутентификация library-service");

    }

    @ExceptionHandler(ExternalServiceInteractionException.class)
    public ResponseEntity<String> handler(ExternalServiceInteractionException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body("Ошибка аутентификация library-service");

    }

    @ExceptionHandler(AuthenticationAdminException.class)
    public ResponseEntity<String> handler(AuthenticationAdminException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(FORBIDDEN).contentType(MediaType.APPLICATION_JSON)
                .body("У вас недостаточно прав для выполнения этой операции");

    }
}
