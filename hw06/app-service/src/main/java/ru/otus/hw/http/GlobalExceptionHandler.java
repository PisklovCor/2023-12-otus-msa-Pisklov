package ru.otus.hw.http;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.AuthenticationException;
import ru.otus.hw.exceptions.EntityNotFoundException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handler(EntityNotFoundException e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body("Ошибка сервера");

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handler(AuthenticationException e) {
        return ResponseEntity.status(UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body("Ошибка аутентификация");

    }
}
