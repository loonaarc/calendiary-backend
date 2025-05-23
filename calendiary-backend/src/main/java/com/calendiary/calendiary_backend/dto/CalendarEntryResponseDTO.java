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
public record CalendarEntryResponseDTO(
        Long id,
        @NotBlank String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String location,
        String label,
        String diaryEntry,
        Double moodRating,
        Long userId
) {
    public static CalendarEntryResponseDTO fromEntity(CalendarEntryEntity entity) {
        return new CalendarEntryResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getLocation(),
                entity.getLabel(),
                entity.getDiaryEntry(),
                entity.getMoodRating(),
                entity.getUserId()
        );
    }
}
