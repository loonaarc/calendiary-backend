package com.calendiary.calendiary_backend.util;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseUtil {

    public static ResponseEntity<?> unauthorized(String message, String details) {
        return ResponseEntity.status(401).body(Map.of("error", message, "details", details));
    }

    public static ResponseEntity<?> notFound(String message, String details) {
        return ResponseEntity.status(404).body(Map.of("error", message, "details", details));
    }

    public static ResponseEntity<?> badRequest(String message, String details) {
        return ResponseEntity.status(400).body(Map.of("error", message, "details", details));
    }

    public static ResponseEntity<?> unprocessable(String message, String details) {
        return ResponseEntity.status(422).body(Map.of("error", message, "details", details));
    }

    public static ResponseEntity<?> ok(Object body) {
        return ResponseEntity.ok(body);
    }
}
