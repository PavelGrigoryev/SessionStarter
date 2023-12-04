package ru.clevertec.starter.exception.handler;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.ResponseErrorHandler;
import ru.clevertec.starter.exception.SessionServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class SessionServiceResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(@NonNull ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(@NonNull ClientHttpResponse response) throws IOException {
        String errorMessage = new BufferedReader(new InputStreamReader(response.getBody()))
                .lines()
                .collect(Collectors.joining());
        throw new SessionServiceException(errorMessage);
    }

}
