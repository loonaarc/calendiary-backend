package com.calendiary.calendiary_backend.exceptions;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException() {
        super("User not authorized");
    }

}
