package com.calendiary.calendiary_backend.repository;

import com.calendiary.calendiary_backend.model.CalendarEntryEntity;
import com.calendiary.calendiary_backend.model.LabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public interface LabelRepository extends JpaRepository<LabelEntity, Long> {
    List<LabelEntity> findByUserId(Long userId);
    Optional<LabelEntity> findByNameAndUserId(String name, Long userId);
    void deleteByUserId(Long userId);
    boolean existsByUserIdAndName(Long userId, String name);

}
