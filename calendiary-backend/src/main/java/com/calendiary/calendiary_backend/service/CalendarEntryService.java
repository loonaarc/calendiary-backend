package com.calendiary.calendiary_backend.service;

import com.calendiary.calendiary_backend.dto.CalendarEntryRequestDTO;
import com.calendiary.calendiary_backend.dto.CalendarEntryResponseDTO;
import com.calendiary.calendiary_backend.exceptions.InvalidUserException;
import com.calendiary.calendiary_backend.model.CalendarEntryEntity;
import com.calendiary.calendiary_backend.repository.CalendarEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


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
        try {
            return repository.findByUserId(Long.parseLong(userId))
                    .stream()
                    .map(CalendarEntryService::fromEntityToResponseDTO)
                    .toList();
        } catch (IllegalArgumentException iae) {
            throw new InvalidUserException();
        }
    }

    public CalendarEntryResponseDTO createEntry(String userId, CalendarEntryRequestDTO dto) throws  InvalidUserException{
        try {
            CalendarEntryEntity entity = repository.save(new CalendarEntryEntity(dto, Long.parseLong(userId)));
            return fromEntityToResponseDTO(entity);
        } catch (IllegalArgumentException iae) {
            throw new InvalidUserException();
        }
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








}
