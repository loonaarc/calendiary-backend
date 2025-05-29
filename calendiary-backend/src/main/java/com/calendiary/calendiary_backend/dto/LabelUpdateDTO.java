package com.calendiary.calendiary_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LabelUpdateDTO(
        String name,
        String color
) {
}
