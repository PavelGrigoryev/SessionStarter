package ru.clevertec.starter.sevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.clevertec.starter.exception.SessionServiceException;
import ru.clevertec.starter.model.Authorization;
import ru.clevertec.starter.model.Session;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class SessionAwareService {

    private final RestClient restClient;

    public Session findByLogin(String login) {
        try {
            return restClient.post()
                    .body(new Authorization(login))
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError, throwSessionServiceException())
                    .body(Session.class);
        } catch (ResourceAccessException e) {
            throw new SessionServiceException("Service with sessions is disabled or not available on this url");
        }
    }

    @Scheduled(cron = "${session.aware.clean.cron}")
    private void clean() {
        try {
            String message = restClient.delete()
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError, throwSessionServiceException())
                    .body(String.class);
            log.warn(message);
        } catch (ResourceAccessException e) {
            throw new SessionServiceException("Service with sessions is disabled or not available on this url");
        }
    }

    private RestClient.ResponseSpec.ErrorHandler throwSessionServiceException() {
        return (request, response) -> {
            String errorMessage = new BufferedReader(new InputStreamReader(response.getBody()))
                    .lines()
                    .collect(Collectors.joining());
            throw new SessionServiceException(errorMessage);
        };
    }

}
