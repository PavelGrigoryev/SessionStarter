package ru.clevertec.session.exception.model;

import java.util.List;

public record ValidationExceptionResponse(List<Violation> violations) {
}
