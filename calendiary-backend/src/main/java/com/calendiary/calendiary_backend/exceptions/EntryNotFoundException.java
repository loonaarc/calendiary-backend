package com.calendiary.calendiary_backend.exceptions;

public class EntryNotFoundException extends RuntimeException {
    public EntryNotFoundException() {
        super("Entry not found");
    }

}
