package com.calendiary.calendiary_backend.exceptions;

public class InvalidQueryFormat extends RuntimeException {
    public InvalidQueryFormat() {
        super("User Authentication failed");
    }
}
