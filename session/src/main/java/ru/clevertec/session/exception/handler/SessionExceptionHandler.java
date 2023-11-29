package ru.clevertec.session.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.session.exception.SessionServiceException;
import ru.clevertec.session.exception.model.ExceptionResponse;

@Slf4j
@ControllerAdvice
public class SessionExceptionHandler {

    @ExceptionHandler(SessionServiceException.class)
    public ResponseEntity<ExceptionResponse> handleServiceException(SessionServiceException exception) {
        return sendResponse(exception.getMessage());
    }

    private ResponseEntity<ExceptionResponse> sendResponse(String message) {
        ExceptionResponse response = new ExceptionResponse(message);
        log.error(response.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
