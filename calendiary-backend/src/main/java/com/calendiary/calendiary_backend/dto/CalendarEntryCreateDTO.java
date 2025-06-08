package com.calendiary.calendiary_backend.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Set;

/*records are used for immutable DTOs
makes all fields private final
generates public getters without "get"
creates constructor with all fields
 */
public record CalendarEntryCreateDTO(
        @NotBlank String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String locationName,
        String fullAddress,
        Double latitude,
        Double longitude,
        Set<String> labels,
        String diaryEntry,
        Double moodRating,
        String stage
) {

}
