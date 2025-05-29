package com.calendiary.calendiary_backend.service;

import com.calendiary.calendiary_backend.dto.CalendarEntryCreateDTO;
import com.calendiary.calendiary_backend.dto.CalendarEntryResponseDTO;
import com.calendiary.calendiary_backend.dto.CalendarEntryUpdateDTO;
import com.calendiary.calendiary_backend.exceptions.EntryNotFoundException;
import com.calendiary.calendiary_backend.exceptions.InvalidQueryFormatException;
import com.calendiary.calendiary_backend.exceptions.UserNotAuthorizedException;
import com.calendiary.calendiary_backend.model.CalendarEntryEntity;
import com.calendiary.calendiary_backend.repository.CalendarEntryRepository;
import jakarta.transaction.Transactional;
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

    public List<CalendarEntryResponseDTO> getEntriesForUser(String userId) throws InvalidQueryFormatException {
            return repository.findByUserId(parseId(userId))
                    .stream()
                    .map(CalendarEntryService::fromEntityToResponseDTO)
                    .toList();
    }

    public CalendarEntryResponseDTO getEntry(String userId, String id) throws InvalidQueryFormatException,
            EntryNotFoundException, UserNotAuthorizedException{
        return fromEntityToResponseDTO(getEntityFromValidIds(userId, id));
    }

    public CalendarEntryResponseDTO createEntry(String userId, CalendarEntryCreateDTO dto) throws InvalidQueryFormatException {
            CalendarEntryEntity entity = repository.save(new CalendarEntryEntity(dto, parseId(userId)));
            return fromEntityToResponseDTO(entity);
    }

    public void deleteEntry(String id, String userId) throws InvalidQueryFormatException,
            EntryNotFoundException, UserNotAuthorizedException {
        repository.delete(getEntityFromValidIds(userId, id));
    }

    @Transactional //required for custom repository methods that modifies records
    public void deleteEntriesForUser(String userId) throws InvalidQueryFormatException {
        repository.deleteByUserId(parseId(userId));
    }

    public CalendarEntryResponseDTO updateEntry(String userId, String id, CalendarEntryUpdateDTO dto) throws InvalidQueryFormatException,
            EntryNotFoundException, UserNotAuthorizedException {
        CalendarEntryEntity entity = getEntityFromValidIds(userId, id);
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

    public static Long parseId(String string) throws InvalidQueryFormatException {
        try {
            return Long.parseLong(string);
        } catch (IllegalArgumentException iae) {
            throw new InvalidQueryFormatException();
        }
    }

    public CalendarEntryEntity getEntityFromValidIds(String userId, String id) throws InvalidQueryFormatException,
            EntryNotFoundException, UserNotAuthorizedException {
        Optional<CalendarEntryEntity> optionalEntity = repository.findById(parseId(id));
        if (optionalEntity.isEmpty()) {
            throw new EntryNotFoundException();
        }
        CalendarEntryEntity entity = optionalEntity.get();
        if (!entity.getUserId().equals(parseId(userId))) {
            throw new UserNotAuthorizedException();
        }
        return entity;
    }








}
