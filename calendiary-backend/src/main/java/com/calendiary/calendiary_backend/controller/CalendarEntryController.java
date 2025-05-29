package com.calendiary.calendiary_backend.controller;

import com.calendiary.calendiary_backend.dto.CalendarEntryCreateDTO;
import com.calendiary.calendiary_backend.dto.CalendarEntryResponseDTO;
import com.calendiary.calendiary_backend.dto.CalendarEntryUpdateDTO;
import com.calendiary.calendiary_backend.security.TokenValidator;
import com.calendiary.calendiary_backend.service.CalendarEntryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //composite annotation of @Controller @ResponseBody
@RequestMapping("/calendar")
@RequiredArgsConstructor //Lobok generates a constructor at compile time with all final fields as parameter
public class CalendarEntryController {
    private final CalendarEntryService service; //looks for @Service-annotated implementation and auto-injects
    private final TokenValidator tokenValidator;

    //Get all entries
    @Operation(summary = "Get total calendar entries (no auth)", tags = {"No Auth Endpoints"})
    @GetMapping("/all-entries-no-auth")
    public List<CalendarEntryResponseDTO> getAllEntries() {
        return service.getAllEntries();
    }

    @Operation(summary = "Get all calendar entries for user (authenticated)", tags = {"Authenticated Endpoints"})
    @GetMapping("/my-entries")
    public ResponseEntity<?> getUserEntries(
            @RequestHeader("Authorization") String authHeader) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        return ResponseEntity.ok(service.getEntriesForUser(userId));
    }

    @Operation(summary = "Get all calendar entries for user (no auth)", tags = {"No Auth Endpoints"})
    @GetMapping("/my-entries-no-auth")
    public ResponseEntity<?> getUserEntriesNoAuth(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(service.getEntriesForUser(userId));
    }

    @Operation(summary = "Get calendar entry by id (authenticated)", tags = {"Authenticated Endpoints"})
    @GetMapping("/my-entries/{id}")
    public ResponseEntity<?> getSingleEntry(
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String authHeader) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        return ResponseEntity.ok(service.getEntry(userId, id));
    }

    @Operation(summary = "Get calendar entry by id (no auth)", tags = {"No Auth Endpoints"})
    @GetMapping("/my-entries-no-auth/{id}")
    public ResponseEntity<?> getSingleEntryNoAuth(
            @PathVariable("id") String id,
            @RequestParam("userId") String userId) {
        return ResponseEntity.ok(service.getEntry(userId, id));
    }

    @Operation(summary = "Create calendar entry (authenticated)", tags = {"Authenticated Endpoints"})
    @PostMapping("/my-entries")
    public ResponseEntity<?> createEntry(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid CalendarEntryCreateDTO body
    ) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        return ResponseEntity.ok(service.createEntry(userId, body));
    }

    @Operation(summary = "Create calendar entry (no auth)", tags = {"No Auth Endpoints"})
    @PostMapping("/my-entries-no-auth")
    public ResponseEntity<?> createEntryNoAuth(
            @RequestParam("userId") String userId,
            @RequestBody @Valid CalendarEntryCreateDTO body
    ) {
        return ResponseEntity.ok(service.createEntry(userId, body));
    }

    @Operation(summary = "Update calendar entry by id (authenticated)", tags = {"Authenticated Endpoints"})
    @PutMapping("/my-entries/{id}")
    public ResponseEntity<?> updateEntry(
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid CalendarEntryUpdateDTO body
    ) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        return ResponseEntity.ok(service.updateEntry(userId, id, body));
    }

    @Operation(summary = "Update calendar entry by id (no auth)", tags = {"No Auth Endpoints"})
    @PutMapping("/my-entries-no-auth/{id}")
    public ResponseEntity<?> updateEntryNoAuth(
            @PathVariable("id") String id,
            @RequestParam("userId") String userId,
            @RequestBody @Valid CalendarEntryUpdateDTO body
    ) {
        return ResponseEntity.ok(service.updateEntry(userId, id, body));
    }

    @Operation(summary = "Delete calendar entry by id (authenticated)", tags = {"Authenticated Endpoints"})
    @DeleteMapping("/my-entries/{id}")
    public ResponseEntity<?> deleteEntry(
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String authHeader
    ) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        service.deleteEntry(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete calendar entry by id (no auth)", tags = {"No Auth Endpoints"})
    @DeleteMapping("/my-entries-no-auth/{id}")
    public ResponseEntity<?> deleteEntryNoAuth(
            @PathVariable("id") String id,
            @RequestParam("userId") String userId
    ) {
        service.deleteEntry(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all calendar entries of user (authenticated)", tags = {"Authenticated Endpoints"})
    @DeleteMapping("/my-entries")
    public ResponseEntity<?> deleteAllEntriesForUser(
            @RequestHeader("Authorization") String authHeader
    ) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        service.deleteEntriesForUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete all calendar entries of user (no auth)", tags = {"No Auth Endpoints"})
    @DeleteMapping("/my-entries-no-auth")
    public ResponseEntity<?> deleteAllEntriesForUserNoAuth(
            @RequestParam("userId") String userId
    ) {
        service.deleteEntriesForUser(userId);
        return ResponseEntity.noContent().build();
    }


}
