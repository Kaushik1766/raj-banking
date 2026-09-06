package com.rajbank.loanapp.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class ValidatorUtil {

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();

    private ValidatorUtil() {
    }

    /**
     * Validates the given bean against its Jakarta Bean Validation annotations
     * and returns a map of property path -> violation message. An empty map
     * means the bean is valid.
     */
    public static <T> Map<String, String> validate(T bean) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(bean);
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<T> violation : violations) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }

    public static <T> boolean isValid(T bean) {
        return VALIDATOR.validate(bean).isEmpty();
    }
}
