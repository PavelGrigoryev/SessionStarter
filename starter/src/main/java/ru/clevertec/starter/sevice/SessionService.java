package ru.clevertec.starter.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import ru.clevertec.starter.model.Session;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SessionService {

    private final Map<String, Session> sessions;

    public SessionService() {
        sessions = new ConcurrentHashMap<>();
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

    @Scheduled(cron = "${session.aware.clean.cron}")
    private void clean() {
        LocalDateTime now = LocalDateTime.now();
        sessions.entrySet()
                .removeIf(entry -> entry.getValue().getOpeningTime().isBefore(now));
        log.warn("Cleared all sessions until {}", now);
    }

}
