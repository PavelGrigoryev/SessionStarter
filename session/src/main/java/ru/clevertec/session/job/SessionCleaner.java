package ru.clevertec.session.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.clevertec.session.service.SessionService;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionCleaner {

    private final SessionService sessionService;

    @Scheduled(cron = "${session.cron}")
    public void clean() {
        LocalDateTime startOfTheDay = LocalDateTime.now();
        sessionService.deleteAllByOpeningTimeBefore(startOfTheDay);
        log.warn("Cleared all sessions until {}", startOfTheDay.toLocalDate());
    }

}
