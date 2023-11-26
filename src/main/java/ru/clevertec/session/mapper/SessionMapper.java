package ru.clevertec.session.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.session.dto.SessionRequest;
import ru.clevertec.session.dto.SessionResponse;
import ru.clevertec.session.model.Session;

@Mapper
public interface SessionMapper {

    Session fromRequest(SessionRequest request);

    SessionResponse toResponse(Session session);

}
