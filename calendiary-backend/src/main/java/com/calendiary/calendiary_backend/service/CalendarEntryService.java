package com.calendiary.calendiary_backend.service;

import com.calendiary.calendiary_backend.dto.CalendarEntryCreateDTO;
import com.calendiary.calendiary_backend.dto.CalendarEntryResponseDTO;
import com.calendiary.calendiary_backend.dto.CalendarEntryUpdateDTO;
import com.calendiary.calendiary_backend.exceptions.EntryNotFoundException;
import com.calendiary.calendiary_backend.exceptions.InvalidQueryFormatException;
import com.calendiary.calendiary_backend.exceptions.UserNotAuthorizedException;
import com.calendiary.calendiary_backend.model.CalendarEntryEntity;
import com.calendiary.calendiary_backend.model.LabelEntity;
import com.calendiary.calendiary_backend.repository.CalendarEntryRepository;
import com.calendiary.calendiary_backend.repository.LabelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor //Lombok generates the constructor with all final fields as parameters
public class CalendarEntryService {
    private final CalendarEntryRepository repository; //final forces us to depend on Spring's dependency injection.
    private final LabelRepository labelRepository;

    // --- New helper method to convert DTO to Entity using Lombok @Builder ---
    private CalendarEntryEntity toEntity(CalendarEntryCreateDTO dto, Long userId) {
        return CalendarEntryEntity.builder()
                .title(dto.title())
                .description(dto.description())
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .locationName(dto.locationName())    // Mapping new location fields from DTO
                .fullAddress(dto.fullAddress())      // Mapping new location fields from DTO
                .latitude(dto.latitude())            // Mapping new location fields from DTO
                .longitude(dto.longitude())          // Mapping new location fields from DTO
                .diaryEntry(dto.diaryEntry())
                .moodRating(dto.moodRating())
                .stage(dto.stage())
                .userId(userId)
                // Use the existing saveNewLabels logic to convert Set<String> to Set<LabelEntity>
                .labels(dto.labels() != null ? saveNewLabels(dto.labels(), String.valueOf(userId)) : new HashSet<>())
                .build();
    }


    public List<CalendarEntryResponseDTO> getAllEntries() {
        return repository.findAll()
                .stream()
                .map(this::fromEntityToResponseDTO) // Changed to use non-static method reference
                .toList();
    }

    public List<CalendarEntryResponseDTO> getEntriesForUser(String userId) throws InvalidQueryFormatException {
        return repository.findByUserId(parseId(userId))
                .stream()
                .map(this::fromEntityToResponseDTO) // Changed to use non-static method reference
                .toList();
    }

    public CalendarEntryResponseDTO getEntry(String userId, String id) throws InvalidQueryFormatException,
            EntryNotFoundException, UserNotAuthorizedException {
        return fromEntityToResponseDTO(getEntityFromValidIds(userId, id)); // Changed to use non-static method reference
    }

    public CalendarEntryResponseDTO createEntry(String userId, CalendarEntryCreateDTO dto) throws InvalidQueryFormatException {
        // Using the new toEntity method that utilizes Lombok @Builder
        CalendarEntryEntity entity = toEntity(dto, parseId(userId));
        // Labels are now handled within toEntity, so this block might be redundant if labels are never null
        // if (dto.labels() != null && !dto.labels().isEmpty()) {
        //     entity.setLabels(saveNewLabels(dto.labels(), userId));
        // }
        repository.save(entity);
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
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        entity.setStartTime(dto.startTime());
        entity.setEndTime(dto.endTime());

        // Update new location fields
        // Handling case where location fields might be explicitly cleared (set to null)
        // If the DTO field is explicitly null, it means the client wants to clear it.
        // If it's not null, but empty string/0, that's what gets set.
        entity.setLocationName(dto.locationName());
        entity.setFullAddress(dto.fullAddress());
        entity.setLatitude(dto.latitude());
        entity.setLongitude(dto.longitude());
        entity.setLabels(saveNewLabels(dto.labels(), userId));
        entity.setDiaryEntry(dto.diaryEntry());
        entity.setMoodRating(dto.moodRating());
        entity.setStage(dto.stage());
        repository.save(entity);
        return fromEntityToResponseDTO(entity);
    }

    public CalendarEntryResponseDTO patchEntry(String userId, String id, CalendarEntryUpdateDTO dto) throws InvalidQueryFormatException,
            EntryNotFoundException, UserNotAuthorizedException {
        CalendarEntryEntity entity = getEntityFromValidIds(userId, id);
        if (dto.title() != null) entity.setTitle(dto.title());
        if (dto.description() != null) entity.setDescription(dto.description());
        if (dto.startTime() != null) entity.setStartTime(dto.startTime());
        if (dto.endTime() != null) entity.setEndTime(dto.endTime());

        // Update new location fields
        // Handling case where location fields might be explicitly cleared (set to null)
        // If the DTO field is explicitly null, it means the client wants to clear it.
        // If it's not null, but empty string/0, that's what gets set.
        if (dto.locationName() != null) entity.setLocationName(dto.locationName());
        if (dto.fullAddress() != null) entity.setFullAddress(dto.fullAddress());
        if (dto.latitude() != null)  entity.setLatitude(dto.latitude());
        if (dto.longitude() != null) entity.setLongitude(dto.longitude());
        if (dto.labels() != null) entity.setLabels(saveNewLabels(dto.labels(), userId));
        if (dto.diaryEntry() != null) entity.setDiaryEntry(dto.diaryEntry());
        if (dto.moodRating() != null) entity.setMoodRating(dto.moodRating());
        if (dto.stage() != null) entity.setStage(dto.stage());
        repository.save(entity);
        return fromEntityToResponseDTO(entity);
    }

    // This method is now non-static to access labelRepository for mapping
    public CalendarEntryResponseDTO fromEntityToResponseDTO(CalendarEntryEntity entity) {
        return new CalendarEntryResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getLocationName(), // Mapping new locationName field
                entity.getFullAddress(),  // Mapping new fullAddress field
                entity.getLatitude(),     // Mapping new latitude field
                entity.getLongitude(),    // Mapping new longitude field
                entity.getLabels().stream()
                        .map(LabelEntity::getName)
                        .collect(Collectors.toSet()),
                entity.getDiaryEntry(),
                entity.getMoodRating(),
                entity.getStage(),
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

    public Set<LabelEntity> saveNewLabels(Set<String> labels, String userId) {
        Set<LabelEntity> labelEntities = new HashSet<>();
        Long parsedUserId = parseId(userId); // Parse userId once here

        for (String labelName : labels) {
            // Try to find existing label by name and userId
            Optional<LabelEntity> label = labelRepository.findByNameAndUserId(labelName, parsedUserId);

            // If not found, create it
            LabelEntity resolved = label.orElseGet(() -> {
                LabelEntity newLabel = new LabelEntity();
                newLabel.setName(labelName);
                newLabel.setUserId(parsedUserId); // Set userId for new labels
                return labelRepository.save(newLabel);
            });

            labelEntities.add(resolved); //links CalendarEntryEntity to labels?
        }

        return labelEntities;
    }
}