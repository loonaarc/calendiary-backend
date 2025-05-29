package com.calendiary.calendiary_backend.service;

import com.calendiary.calendiary_backend.dto.CalendarEntryRequestDTO;
import com.calendiary.calendiary_backend.dto.CalendarEntryResponseDTO;
import com.calendiary.calendiary_backend.dto.CalendarEntryUpdateDTO;
import com.calendiary.calendiary_backend.exceptions.EntryNotFoundException;
import com.calendiary.calendiary_backend.exceptions.InvalidUserException;
import com.calendiary.calendiary_backend.exceptions.UserNotAuthorizedException;
import com.calendiary.calendiary_backend.model.CalendarEntryEntity;
import com.calendiary.calendiary_backend.repository.CalendarEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor //Lombok generates the constructor with all final fields as parameters
public class CalendarEntryService {
    private final CalendarEntryRepository repository; //final forces us to depend on Spring's dependency injection.

    public List<CalendarEntryResponseDTO> getAllEntries() {
        return repository.findAll()
                .stream()
                .map(CalendarEntryService::fromEntityToResponseDTO)
                .toList();
    }

    public List<CalendarEntryResponseDTO> getEntriesForUser(String userId) throws InvalidUserException{
            return repository.findByUserId(parseUserId(userId))
                    .stream()
                    .map(CalendarEntryService::fromEntityToResponseDTO)
                    .toList();
    }

    public CalendarEntryResponseDTO createEntry(String userId, CalendarEntryRequestDTO dto) throws  InvalidUserException{
            CalendarEntryEntity entity = repository.save(new CalendarEntryEntity(dto, parseUserId(userId)));
            return fromEntityToResponseDTO(entity);
    }

    public CalendarEntryResponseDTO updateEntry(String userId, CalendarEntryUpdateDTO dto) throws InvalidUserException,
            EntryNotFoundException, UserNotAuthorizedException {
       Optional<CalendarEntryEntity> optionalEntity = repository.findById(dto.id());
        if (optionalEntity.isEmpty()) {
            throw new EntryNotFoundException();
        }
        CalendarEntryEntity entity = optionalEntity.get();
        if (!entity.getUserId().equals(parseUserId(userId))) {
            throw new UserNotAuthorizedException();
        }
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.description() != null) entity.setDescription(dto.description());
        if (dto.startTime() != null) entity.setStartTime(dto.startTime());
        if (dto.endTime() != null) entity.setEndTime(dto.endTime());
        if (dto.location() != null) entity.setLocation(dto.title());
        if (dto.label() != null) entity.setLabel(dto.label());
        if (dto.diaryEntry() != null) entity.setDiaryEntry(dto.diaryEntry());
        if (dto.moodRating() != null) entity.setMoodRating(dto.moodRating());
        repository.save(entity);
        return fromEntityToResponseDTO(entity);
    }

    public static CalendarEntryResponseDTO fromEntityToResponseDTO(CalendarEntryEntity entity) {
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

    public static Long parseUserId(String string) throws InvalidUserException{
        try {
            return Long.parseLong(string);
        } catch (IllegalArgumentException iae) {
            throw new InvalidUserException();
        }
    }








}
