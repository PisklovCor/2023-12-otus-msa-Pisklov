package ru.otus.hw.exceptions;

public class IdempotentRequestsException extends RuntimeException {
    public IdempotentRequestsException(String message) {
        super(message);
    }
}
