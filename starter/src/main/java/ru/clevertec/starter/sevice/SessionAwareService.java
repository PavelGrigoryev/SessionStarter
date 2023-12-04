package ru.clevertec.starter.sevice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.clevertec.starter.exception.SessionServiceException;
import ru.clevertec.starter.exception.handler.SessionServiceResponseErrorHandler;
import ru.clevertec.starter.model.Authorization;
import ru.clevertec.starter.model.BlackListResponse;
import ru.clevertec.starter.model.Session;

@RequiredArgsConstructor
public class SessionAwareService {

    private final RestClient restClient;
    private final SessionServiceResponseErrorHandler errorHandler;

    private static final String ACCESS_EXCEPTION_MESSAGE = "Service with sessions is disabled or not available on this url";

    public Session findByLogin(String login) {
        try {
            return restClient.post()
                    .body(new Authorization(login))
                    .retrieve()
                    .onStatus(errorHandler)
                    .body(Session.class);
        } catch (ResourceAccessException e) {
            throw new SessionServiceException(ACCESS_EXCEPTION_MESSAGE);
        }
    }

    public BlackListResponse findAllBlackLists() {
        try {
            return restClient.get()
                    .retrieve()
                    .onStatus(errorHandler)
                    .body(BlackListResponse.class);
        } catch (ResourceAccessException e) {
            throw new SessionServiceException(ACCESS_EXCEPTION_MESSAGE);
        }
    }

}
