package com.calendiary.calendiary_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CalendarEntryUpdateDTO(
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String location,
        String label,
        String diaryEntry,
        Double moodRating
) {

}
