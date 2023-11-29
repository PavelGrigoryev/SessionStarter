package ru.clevertec.starter.sevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestClient;
import ru.clevertec.starter.model.Authorization;
import ru.clevertec.starter.model.Session;

@Slf4j
@RequiredArgsConstructor
public class SessionAwareService {

    private final RestClient restClient;

    public Session findByLogin(String login) {
        return restClient.post()
                .body(new Authorization(login))
                .retrieve()
                .body(Session.class);
    }

    @Scheduled(cron = "${session.aware.clean.cron}")
    private void clean() {
        String message = restClient.delete()
                .retrieve()
                .body(String.class);
        log.warn(message);
    }

}
