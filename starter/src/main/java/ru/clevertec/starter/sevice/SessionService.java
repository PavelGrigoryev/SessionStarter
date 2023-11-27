package ru.clevertec.starter.sevice;

import ru.clevertec.starter.model.Session;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SessionService {

    private final Map<String, Session> sessions;

    public SessionService() {
        sessions = new HashMap<>();
    }

    public Session save(String login) {
        Session session = new Session()
                .setId(UUID.randomUUID().toString())
                .setLogin(login)
                .setOpeningTime(LocalDateTime.now());
        sessions.put(login, session);
        return session;
    }

    public Session findByLogin(String login) {
        Session session = sessions.get(login);
        if (Objects.isNull(session)) {
            session = save(login);
        }
        return session;
    }

}
