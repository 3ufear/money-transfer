package ru.bank.exception;

import io.micronaut.http.HttpStatus;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public BusinessException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
