package com.calendiary.calendiary_backend.dto;

import com.calendiary.calendiary_backend.model.CalendarEntryEntity;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

/*records are used for immutable DTOs
makes all fields private final
generates public getters without "get"
creates constructor with all fields
 */
public record CreateCalendarEntryDTO(
        @NotBlank String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String location,
        String label,
        String diaryEntry,
        double moodRating,
        UUID userId
) {

}
