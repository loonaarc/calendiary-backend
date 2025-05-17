package com.calendiary.calendiary_backend.repository;

import com.calendiary.calendiary_backend.model.CalendiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CalendiaryEntryRepository extends JpaRepository<CalendiaryEntry, UUID> {
    List<CalendiaryEntry> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

}
