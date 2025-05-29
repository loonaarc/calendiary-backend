package com.calendiary.calendiary_backend.exceptions;

public class LabelExistsException extends RuntimeException {
    public LabelExistsException() {
        super("User Authentication failed");
    }
}
