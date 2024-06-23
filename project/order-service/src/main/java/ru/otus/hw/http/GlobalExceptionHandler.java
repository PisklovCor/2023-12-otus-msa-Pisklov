package ru.otus.hw.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.AuthenticationAdminException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.ExternalServiceInteractionException;
import ru.otus.hw.exceptions.IdempotentRequestsException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.FORBIDDEN;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handler(Exception e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON).body("Ошибка сервера order-service");

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handler(EntityNotFoundException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON).body("Ошибка поиска order-service");

    }

    @ExceptionHandler(ExternalServiceInteractionException.class)
    public ResponseEntity<String> handler(ExternalServiceInteractionException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON).body("Ошибка обращения к внешнему сервису");

    }

    @ExceptionHandler(IdempotentRequestsException.class)
    public ResponseEntity<String> handler(IdempotentRequestsException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON).body("Ошибка идемпотентности order-service");

    }

    @ExceptionHandler(AuthenticationAdminException.class)
    public ResponseEntity<String> handler(AuthenticationAdminException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(FORBIDDEN).contentType(MediaType.APPLICATION_JSON)
                .body("У вас недостаточно прав для выполнения этой операции");

    }
}
