package ru.clevertec.session.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record SessionResponse(Long id,
                              String login,

                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
                              LocalDateTime openingTime) {
}
