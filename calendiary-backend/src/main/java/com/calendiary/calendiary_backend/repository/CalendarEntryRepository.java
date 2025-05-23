package com.calendiary.calendiary_backend.repository;

import com.calendiary.calendiary_backend.model.CalendarEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/*
Spring generates proxy class "CalendiaryEntryRepositoryImpl implements" which will inherit the methods of
JpaRepository, like:
    - save()
    - findAll()
    - findByStartTimeBetween()
    - [All other JPA methods]
 */
@Repository
public interface CalendarEntryRepository extends JpaRepository<CalendarEntryEntity, UUID> {
    List<CalendarEntryEntity> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    List<CalendarEntryEntity> findByUserId(Long userId);
}
