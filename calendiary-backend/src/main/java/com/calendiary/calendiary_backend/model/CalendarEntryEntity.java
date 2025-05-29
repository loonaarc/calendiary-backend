package com.calendiary.calendiary_backend.model;


import com.calendiary.calendiary_backend.dto.CalendarEntryRequestDTO;
import com.calendiary.calendiary_backend.dto.CalendarEntryResponseDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
public class CalendarEntryEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //for ID generation of type long and integer
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @Column(columnDefinition = "TEXT") //column type for long entries
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String label;

    @Column(columnDefinition = "TEXT")
    private String diaryEntry;

    @Min(0) @Max(5)
    private Double moodRating;

    private Long userId;

    public CalendarEntryEntity(CalendarEntryRequestDTO dto) {
        title = dto.title();
        description = dto.description();
        startTime = dto.startTime();
        endTime = dto.endTime();
        location = dto.location();
        label = dto.label();
        diaryEntry = dto.diaryEntry();
        moodRating = dto.moodRating();
    }
}
