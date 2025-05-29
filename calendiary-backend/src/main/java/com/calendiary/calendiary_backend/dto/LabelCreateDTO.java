package com.calendiary.calendiary_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record LabelCreateDTO(
        @NotBlank String name,
        String color
) {
}
