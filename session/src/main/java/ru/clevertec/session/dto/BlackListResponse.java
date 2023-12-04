package ru.clevertec.session.dto;

import java.util.Set;

public record BlackListResponse(Set<String> logins) {
}
