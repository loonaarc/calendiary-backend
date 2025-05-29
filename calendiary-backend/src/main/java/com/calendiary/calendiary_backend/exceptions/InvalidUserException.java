package com.calendiary.calendiary_backend.exceptions;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("User Authentication failed");
    }
}
