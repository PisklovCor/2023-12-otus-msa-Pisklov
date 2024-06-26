package ru.otus.hw.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.AuthenticationException;
import ru.otus.hw.exceptions.AuthUserNotFoundException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.AuthenticationAdminException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handler(Exception e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body("Ошибка сервера account-service");

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handler(EntityNotFoundException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(SERVICE_UNAVAILABLE).contentType(MediaType.APPLICATION_JSON)
                .body("Ошибка поиска account-service");

    }

    @ExceptionHandler(AuthUserNotFoundException.class)
    public ResponseEntity<String> handler(AuthUserNotFoundException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body("Ошибка при поиске пользователя account-service");

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handler(AuthenticationException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
                .body("Ошибка аутентификация account-service");

    }

    @ExceptionHandler(AuthenticationAdminException.class)
    public ResponseEntity<String> handler(AuthenticationAdminException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(FORBIDDEN).contentType(MediaType.APPLICATION_JSON)
                .body("У вас недостаточно прав для выполнения этой операции");

    }
}
