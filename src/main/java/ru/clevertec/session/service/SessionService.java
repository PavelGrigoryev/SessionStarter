package ru.clevertec.session.service;

import ru.clevertec.session.dto.SessionRequest;
import ru.clevertec.session.dto.SessionResponse;

import java.time.LocalDateTime;

public interface SessionService {

    SessionResponse save(SessionRequest request);

    SessionResponse findByLogin(SessionRequest request);

    void deleteAllByOpeningTimeBefore(LocalDateTime dateTime);

}
