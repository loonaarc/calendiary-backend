package com.calendiary.calendiary_backend.service;

import com.calendiary.calendiary_backend.dto.CalendarEntryResponseDTO;
import com.calendiary.calendiary_backend.dto.CreateCalendarEntryDTO;
import com.calendiary.calendiary_backend.exceptions.DatabaseException;
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
                .map(this::fromEntity)
                .toList();
    }

    public List<CalendarEntryResponseDTO> getEntriesForUser(String userId) throws DatabaseException {
        try {
            return repository.findByUserId(Long.parseLong(userId))
                    .stream()
                    .map(this::fromEntity)
                    .toList();
        } catch (IllegalArgumentException iae) {
            throw new DatabaseException();
        }

    }

    public CalendarEntryResponseDTO fromEntity(CalendarEntryEntity entity) {
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



/*
    public CalendarEntryDTO createEntry(CalendarEntryDTO dto, String token) {
        //Validate token and get user info
        UserInfo user = authClient.validateToken(token);

        CalendarEntryEntity entity = convertT
    }
*/


}
