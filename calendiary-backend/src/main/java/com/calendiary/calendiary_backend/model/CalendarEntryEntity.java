package com.calendiary.calendiary_backend.model;


import com.calendiary.calendiary_backend.dto.CalendarEntryCreateDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @Table(name = "calendar_entries")
public class CalendarEntryEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //for ID generation of type long and integer
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @Column(columnDefinition = "TEXT") //column type for long entries
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    //private String label;

    private String locationName;
    private String fullAddress;
    private Double latitude;
    private Double longitude;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "entry_label", // name of the table in the database that links entries and labels
            joinColumns = @JoinColumn(name = "entry_id"), // refers to this entity (CalendarEntry)
            inverseJoinColumns = @JoinColumn(name = "label_id") // refers to the other entity (Label)
    )
    private Set<LabelEntity> labels = new HashSet<>(); //collection of unique labels

    @Column(columnDefinition = "TEXT")
    private String diaryEntry;

    @Min(0) @Max(5)
    private Double moodRating;

    private Long userId;
}
