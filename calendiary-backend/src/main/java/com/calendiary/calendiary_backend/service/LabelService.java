package com.calendiary.calendiary_backend.service;

import com.calendiary.calendiary_backend.dto.LabelCreateDTO;
import com.calendiary.calendiary_backend.dto.LabelResponseDTO;
import com.calendiary.calendiary_backend.dto.LabelUpdateDTO;
import com.calendiary.calendiary_backend.exceptions.EntryNotFoundException;
import com.calendiary.calendiary_backend.exceptions.InvalidQueryFormatException;
import com.calendiary.calendiary_backend.exceptions.LabelExistsException;
import com.calendiary.calendiary_backend.exceptions.UserNotAuthorizedException;
import com.calendiary.calendiary_backend.model.LabelEntity;
import com.calendiary.calendiary_backend.repository.LabelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository repository; //final forces us to depend on Spring's dependency injection.

    public List<LabelResponseDTO> getAllEntries() {
        return repository.findAll()
                .stream()
                .map(LabelService::fromEntityToResponseDTO)
                .toList();
    }

    public List<LabelResponseDTO> getEntriesForUser(String userId) throws InvalidQueryFormatException {
        return repository.findByUserId(parseId(userId))
                .stream()
                .map(LabelService::fromEntityToResponseDTO)
                .toList();
    }

    public LabelResponseDTO getEntry(String userId, String id) throws InvalidQueryFormatException,
            EntryNotFoundException, UserNotAuthorizedException {
        return fromEntityToResponseDTO(getEntityFromValidIds(userId, id));
    }

    public LabelResponseDTO createEntry(String userId, LabelCreateDTO dto) throws InvalidQueryFormatException {
        if (repository.existsByUserIdAndName(parseId(userId), dto.name())) {
            throw new LabelExistsException();
        }
        LabelEntity entity = repository.save(new LabelEntity(dto, parseId(userId)));
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
       if (repository.existsByUserIdAndName(parseId(userId), dto.name())) {
            throw new LabelExistsException();
        }
        if (dto.name() != null) entity.setName(dto.name());
        if (dto.color() != null) entity.setColor(dto.color());
        repository.save(entity);
        return fromEntityToResponseDTO(entity);
    }


    public static LabelResponseDTO fromEntityToResponseDTO(LabelEntity entity) {
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


}





