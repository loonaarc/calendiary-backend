package com.calendiary.calendiary_backend.controller;

import com.calendiary.calendiary_backend.dto.CalendarEntryResponseDTO;
import com.calendiary.calendiary_backend.exceptions.DatabaseException;
import com.calendiary.calendiary_backend.security.TokenValidator;
import com.calendiary.calendiary_backend.service.CalendarEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor //Lobok generates a constructor at compile time with all final fields as parameter
public class CalendarEntryController {
    private final CalendarEntryService service; //looks for @Service-annotated implementation and auto-injects
    private final TokenValidator tokenValidator;

    //Get all entries
    @GetMapping("/all-entries")
    public List<CalendarEntryResponseDTO> getAllEntries() {
        return service.getAllEntries();
    }

    @GetMapping("/my-entries")
    public ResponseEntity<?> getUserEntries(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String userId = tokenValidator.validateAndGetUserId(authHeader);
            return ResponseEntity.ok(service.getEntriesForUser(userId));
        } catch (SecurityException se) {
            return ResponseEntity.status(401).body(
                    Map.of("error", "Invalid token", "details", se.getMessage())
            );
        } catch (DatabaseException de) {
            ResponseEntity.status(404).body(
                    Map.of("error", "Invalid username", "details", de.getMessage())
            );
        }
        return null;
    }


}
