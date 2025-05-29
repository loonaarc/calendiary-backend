package com.calendiary.calendiary_backend.controller;

import com.calendiary.calendiary_backend.dto.LabelCreateDTO;
import com.calendiary.calendiary_backend.dto.LabelUpdateDTO;
import com.calendiary.calendiary_backend.dto.LabelResponseDTO;
import com.calendiary.calendiary_backend.security.TokenValidator;
import com.calendiary.calendiary_backend.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labels")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService service;
    private final TokenValidator tokenValidator;

    @Operation(summary = "Get total labels (no auth)", tags = {"No Auth Endpoints"})
    @GetMapping("/all-no-auth")
    public List<LabelResponseDTO> getAllEntries() {
        return service.getAllEntries();
    }

    @Operation(summary = "Get all labels for user (authenticated)", tags = {"Authenticated Endpoints"})
    @GetMapping("")
    public ResponseEntity<?> getUserEntries(
            @RequestHeader("Authorization") String authHeader) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        return ResponseEntity.ok(service.getEntriesForUser(userId));
    }

    @Operation(summary = "Get all labels for user (no auth)", tags = {"No Auth Endpoints"})
    @GetMapping("/no-auth")
    public ResponseEntity<?> getUserEntriesNoAuth(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(service.getEntriesForUser(userId));
    }

    @Operation(summary = "Get label by id (authenticated)", tags = {"Authenticated Endpoints"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getSingleEntry(
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String authHeader) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        return ResponseEntity.ok(service.getEntry(userId, id));
    }

    @Operation(summary = "Get label by id (no auth)", tags = {"No Auth Endpoints"})
    @GetMapping("/no-auth/{id}")
    public ResponseEntity<?> getSingleEntryNoAuth(
            @PathVariable("id") String id,
            @RequestParam("userId") String userId) {
        return ResponseEntity.ok(service.getEntry(userId, id));
    }

    @Operation(summary = "Create label (authenticated)", tags = {"Authenticated Endpoints"})
    @PostMapping("")
    public ResponseEntity<?> createEntry(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid LabelCreateDTO body
    ) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createEntry(userId, body));
    }

    @Operation(summary = "Create label (no auth)", tags = {"No Auth Endpoints"})
    @PostMapping("/no-auth")
    public ResponseEntity<?> createEntryNoAuth(
            @RequestParam("userId") String userId,
            @RequestBody @Valid LabelCreateDTO body
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createEntry(userId, body));
    }

    @Operation(summary = "Update label by id (authenticated)", tags = {"Authenticated Endpoints"})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEntry(
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid LabelUpdateDTO body
    ) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        return ResponseEntity.ok(service.updateEntry(userId, id, body));
    }

    @Operation(summary = "Update label by id (no auth)", tags = {"No Auth Endpoints"})
    @PutMapping("/no-auth/{id}")
    public ResponseEntity<?> updateEntryNoAuth(
            @PathVariable("id") String id,
            @RequestParam("userId") String userId,
            @RequestBody @Valid LabelUpdateDTO body
    ) {
        return ResponseEntity.ok(service.updateEntry(userId, id, body));
    }

    @Operation(summary = "Delete label by id (authenticated)", tags = {"Authenticated Endpoints"})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntry(
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String authHeader
    ) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        service.deleteEntry(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete label by id (no auth)", tags = {"No Auth Endpoints"})
    @DeleteMapping("/no-auth/{id}")
    public ResponseEntity<?> deleteEntryNoAuth(
            @PathVariable("id") String id,
            @RequestParam("userId") String userId
    ) {
        service.deleteEntry(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all labels of user (authenticated)", tags = {"Authenticated Endpoints"})
    @DeleteMapping("")
    public ResponseEntity<?> deleteAllEntriesForUser(
            @RequestHeader("Authorization") String authHeader
    ) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        service.deleteEntriesForUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all labels of user (no auth)", tags = {"No Auth Endpoints"})
    @DeleteMapping("/no-auth")
    public ResponseEntity<?> deleteAllEntriesForUserNoAuth(
            @RequestParam("userId") String userId
    ) {
        service.deleteEntriesForUser(userId);
        return ResponseEntity.noContent().build();
    }
}
