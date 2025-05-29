package com.calendiary.calendiary_backend.dto;

public record LabelResponseDTO(
        Long id,
        String name,
        String color,
        Long userId
) {
}
