package com.calendiary.calendiary_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public record CalendarEntryUpdateDTO(
        String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String location,
        Set<String> labels,
        String diaryEntry,
        Double moodRating
) {

}
