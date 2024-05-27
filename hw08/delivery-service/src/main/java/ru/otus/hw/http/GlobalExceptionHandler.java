package ru.otus.hw.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.exceptions.EntityNotFoundException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handler(Exception e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON).body("Ошибка сервера delivery-service");

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handler(EntityNotFoundException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON).body("Ошибка поиска delivery-service");

    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handler(JsonProcessingException e) {
        log.error("Ошибка: {}", e.getMessage());
        return ResponseEntity.status(NOT_IMPLEMENTED)
                .contentType(MediaType.APPLICATION_JSON).body("Ошибка преобразования delivery-service");

    }
}
