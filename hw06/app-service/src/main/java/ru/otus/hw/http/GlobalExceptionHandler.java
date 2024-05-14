package ru.otus.hw.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.AuthenticationException;
import ru.otus.hw.exceptions.EntityNotFoundException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handler(Exception e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body("Ошибка сервера app-service");

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handler(EntityNotFoundException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(SERVICE_UNAVAILABLE).contentType(MediaType.APPLICATION_JSON).body("Ошибка поиска app-service");

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handler(AuthenticationException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body("Ошибка аутентификация app-service");

    }
}
