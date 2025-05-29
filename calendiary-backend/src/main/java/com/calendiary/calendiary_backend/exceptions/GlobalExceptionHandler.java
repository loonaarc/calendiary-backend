package com.calendiary.calendiary_backend.exceptions;

import com.calendiary.calendiary_backend.util.ResponseUtil;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //on uncaught exception Rest Controller consults this class
public class GlobalExceptionHandler {
    @ExceptionHandler(SecurityException.class)
    public Object handleSecurityException(SecurityException e) {
        return ResponseUtil.unauthorized("Invalid token", e.getMessage());
    }

    @ExceptionHandler(InvalidQueryFormatException.class)
    public Object handleInvalidUserException(InvalidQueryFormatException e) {
        return ResponseUtil.notFound("Invalid query format", e.getMessage());
    }

    @ExceptionHandler(EntryNotFoundException.class)
    public Object handleEntryNotFoudException(EntryNotFoundException e) {
        return ResponseUtil.notFound("Entry not found", e.getMessage());
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    public Object handleUserNotAuthorizedException(UserNotAuthorizedException e) {
        return ResponseUtil.notFound("User not authorized", e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraintViolation(ConstraintViolationException e) {
        return ResponseUtil.badRequest("Constraint violation", e.getMessage());
    }
}
