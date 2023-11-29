package ru.clevertec.session.service;

import ru.clevertec.session.dto.SessionRequest;
import ru.clevertec.session.dto.SessionResponse;

public interface SessionService {

    SessionResponse findByLoginOrSaveAndReturn(SessionRequest request);

    String deleteAll();

}
