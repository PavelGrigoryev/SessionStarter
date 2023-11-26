package ru.clevertec.session.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SessionRequest(@NotBlank
                             @Size(max = 50)
                             String login) {
}
