package com.calendiary.calendiary_backend.feign;

public record AuthResponse (
    String userId, //must match what auth backend returns
    String username, //optional
    boolean isValid
) {}
