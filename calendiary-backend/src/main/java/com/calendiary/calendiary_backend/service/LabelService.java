package com.calendiary.calendiary_backend.service;

import com.calendiary.calendiary_backend.dto.LabelCreateDTO;
import com.calendiary.calendiary_backend.dto.LabelResponseDTO;
import com.calendiary.calendiary_backend.dto.LabelUpdateDTO;
import com.calendiary.calendiary_backend.exceptions.EntryNotFoundException; // Keep this as requested
import com.calendiary.calendiary_backend.exceptions.InvalidQueryFormatException;
import com.calendiary.calendiary_backend.exceptions.LabelExistsException;
import com.calendiary.calendiary_backend.exceptions.UserNotAuthorizedException;
import com.calendiary.calendiary_backend.model.LabelEntity;
import com.calendiary.calendiary_backend.repository.LabelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor //Lombok generates the constructor with all final fields as parameters
public class LabelService {

    private final LabelRepository repository; //final forces us to depend on Spring's dependency injection.

    private static final List<Map<String, String>> DEFAULT_LABELS_DATA = Arrays.asList(
            Map.of("name", "Study", "color", "#90caf9"),
            Map.of("name", "Work", "color", "#ffd180"),
            Map.of("name", "Personal Appointment", "color", "#ce93d8"),
            Map.of("name", "Healthcare", "color", "#ef9a9a"),
            Map.of("name", "Sport Activity", "color", "#a5d6a7"),
            Map.of("name", "Travel", "color", "#80cbc4"),
            Map.of("name", "Other", "color", "#eeeeee")
    );

    public static final String DEFAULT_NEW_TAG_COLOR = "#dcedc8";

    // Helper method to convert LabelCreateDTO to LabelEntity using Lombok @Builder
    private LabelEntity toEntity(LabelCreateDTO dto, Long userId) {
        return LabelEntity.builder()
                .name(dto.name())
                .color(dto.color())
                .userId(userId)
                .build();
    }

    public List<LabelResponseDTO> getAllEntries() {
        return repository.findAll()
                .stream()
                .map(this::fromEntityToResponseDTO) // Changed to use non-static method reference
                .toList();
    }

    public List<LabelResponseDTO> getEntriesForUser(String userId) throws InvalidQueryFormatException {
        return repository.findByUserId(parseId(userId))
                .stream()
                .map(this::fromEntityToResponseDTO) // Changed to use non-static method reference
                .toList();
    }

    public LabelResponseDTO getEntry(String userId, String id) throws InvalidQueryFormatException,
            EntryNotFoundException, UserNotAuthorizedException {
        return fromEntityToResponseDTO(getEntityFromValidIds(userId, id)); // Changed to use non-static method reference
    }

    public LabelResponseDTO createEntry(String userId, LabelCreateDTO dto) throws InvalidQueryFormatException {
        Long parsedUserId = parseId(userId);
        if (repository.existsByUserIdAndName(parsedUserId, dto.name())) {
            throw new LabelExistsException();
        }
        // Using the new toEntity method that utilizes Lombok @Builder
        LabelEntity entity = repository.save(toEntity(dto, parsedUserId));
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

    public LabelResponseDTO updateEntry(String userId, String id, LabelUpdateDTO dto) throws InvalidQueryFormatException,
            EntryNotFoundException, UserNotAuthorizedException {
        LabelEntity entity = getEntityFromValidIds(userId, id);
        Long parsedUserId = parseId(userId);

        // Check if the name is being updated and if the new name already exists for another label
        if (dto.name() != null && !dto.name().equals(entity.getName())) {
            // Only throw LabelExistsException if a DIFFERENT label with the new name already exists for this user
            if (repository.existsByUserIdAndName(parsedUserId, dto.name())) {
                throw new LabelExistsException();
            }
            entity.setName(dto.name());
        }

        entity.setColor(dto.color());
        repository.save(entity);
        return fromEntityToResponseDTO(entity);
    }

    public LabelResponseDTO patchEntry(String userId, String id, LabelUpdateDTO dto) throws InvalidQueryFormatException,
            EntryNotFoundException, UserNotAuthorizedException {
        LabelEntity entity = getEntityFromValidIds(userId, id);
        Long parsedUserId = parseId(userId);

        // Check if the name is being updated and if the new name already exists for another label
        if (dto.name() != null && !dto.name().equals(entity.getName())) {
            // Only throw LabelExistsException if a DIFFERENT label with the new name already exists for this user
            if (repository.existsByUserIdAndName(parsedUserId, dto.name())) {
                throw new LabelExistsException();
            }
            entity.setName(dto.name());
        }
        // If dto.name() is null, or if it's the same as entity.getName(), do nothing with the name.

        if (dto.color() != null) entity.setColor(dto.color());
        repository.save(entity);
        return fromEntityToResponseDTO(entity);
    }

    // Changed to non-static method
    public LabelResponseDTO fromEntityToResponseDTO(LabelEntity entity) {
        return new LabelResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getColor(),
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

    public LabelEntity getEntityFromValidIds(String userId, String id) throws InvalidQueryFormatException,
            EntryNotFoundException, UserNotAuthorizedException {
        Optional<LabelEntity> optionalEntity = repository.findById(parseId(id));
        if (optionalEntity.isEmpty()) {
            throw new EntryNotFoundException();
        }
        LabelEntity entity = optionalEntity.get();
        if (!entity.getUserId().equals(parseId(userId))) {
            throw new UserNotAuthorizedException();
        }
        return entity;
    }

    /**
     * Creates default labels for a given user if they don't have any labels yet.
     * This method is designed to be idempotent: it will only create labels if the user
     * has no existing labels in the database.
     * @param userId The ID of the user for whom to create default labels.
     * @throws InvalidQueryFormatException if the userId cannot be parsed.
     */
    public void createDefaultLabelsForUser(String userId) throws InvalidQueryFormatException {
        Long userLongId = parseId(userId);

        List<LabelEntity> existingLabels = repository.findByUserId(userLongId);

        if (existingLabels.isEmpty()) {
            System.out.println("Creating default labels for user: " + userId);
            for (Map<String, String> data : DEFAULT_LABELS_DATA) {
                LabelCreateDTO defaultLabelDto = new LabelCreateDTO(data.get("name"), data.get("color"));
                try {
                    createEntry(userId, defaultLabelDto);
                } catch (LabelExistsException e) {
                    System.err.println("Attempted to create a default label that already exists for user " + userId + ": " + defaultLabelDto.name());
                }
            }
        } else {
            System.out.println("User " + userId + " already has labels. Skipping default label creation.");
        }
    }
}