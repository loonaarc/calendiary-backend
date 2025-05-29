package com.calendiary.calendiary_backend.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/*records are used for immutable DTOs
makes all fields private final
generates public getters without "get"
creates constructor with all fields
 */
public record CalendarEntryRequestDTO(
        @NotBlank String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String location,
        String label,
        String diaryEntry,
        Double moodRating
) {

}
