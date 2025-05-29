package com.calendiary.calendiary_backend.exceptions;

import com.calendiary.calendiary_backend.util.ResponseUtil;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SecurityException.class)
    public Object handleSecurityException(SecurityException e) {
        return ResponseUtil.unauthorized("Invalid token", e.getMessage());
    }

    @ExceptionHandler(InvalidUserException.class)
    public Object handlInvalidUserException(InvalidUserException e) {
        return ResponseUtil.notFound("Invalid user", e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraintViolation(ConstraintViolationException e) {
        return ResponseUtil.badRequest("Constraint violation", e.getMessage());
    }
}
