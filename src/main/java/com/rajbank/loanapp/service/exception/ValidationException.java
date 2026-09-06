package com.rajbank.loanapp.service.exception;

import java.util.Collections;
import java.util.Map;

/** Raised when input fails a business or bean-validation rule before it reaches the DAO layer. */
public class ValidationException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(message);
        this.fieldErrors = Collections.emptyMap();
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
