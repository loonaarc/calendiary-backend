package com.calendiary.calendiary_backend.controller;

import com.calendiary.calendiary_backend.dto.CalendarEntryRequestDTO;
import com.calendiary.calendiary_backend.dto.CalendarEntryResponseDTO;
import com.calendiary.calendiary_backend.security.TokenValidator;
import com.calendiary.calendiary_backend.service.CalendarEntryService;
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
    @GetMapping("/all-entries-no-auth")
    public List<CalendarEntryResponseDTO> getAllEntries() {
        return service.getAllEntries();
    }

    @GetMapping("/my-entries")
    public ResponseEntity<?> getUserEntries(
            @RequestHeader("Authorization") String authHeader) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        return ResponseEntity.ok(service.getEntriesForUser(userId));
    }

    @GetMapping("/my-entries-no-auth")
    public ResponseEntity<?> getUserEntriesNoAuth(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(service.getEntriesForUser(userId));
    }

    @PostMapping("/my-entries")
    public ResponseEntity<?> createEntry(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid CalendarEntryRequestDTO body
    ) {
        String userId = tokenValidator.validateAndGetUserId(authHeader);
        return ResponseEntity.ok(service.createEntry(userId, body));
    }


    @PostMapping("/my-entries-no-auth")
    public ResponseEntity<?> createEntryNoAuth(
            @RequestParam("userId") String userId,
            @RequestBody @Valid CalendarEntryRequestDTO body
    ) {
        return ResponseEntity.ok(service.createEntry(userId, body));
    }


}
