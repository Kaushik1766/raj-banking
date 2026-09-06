package com.rajbank.loanapp.service.exception;

/** Raised when a lookup (customer id, application number, ...) does not resolve to a record. */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
