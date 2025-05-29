package com.calendiary.calendiary_backend.exceptions;

public class InvalidQueryFormatException extends RuntimeException {
    public InvalidQueryFormatException() {
        super("User Authentication failed");
    }
}
