package ru.otus.hw.exceptions;

public class ExternalServiceInteractionException extends RuntimeException {
    public ExternalServiceInteractionException(String message) {
        super(message);
    }
}
