package ru.clevertec.session.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.session.exception.SessionNotFoundException;
import ru.clevertec.session.exception.SessionServiceException;
import ru.clevertec.session.exception.model.ExceptionResponse;

@Slf4j
@ControllerAdvice
public class SessionExceptionHandler {

    @ExceptionHandler(SessionServiceException.class)
    public ResponseEntity<ExceptionResponse> handleServiceException(SessionServiceException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(SessionNotFoundException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ExceptionResponse> sendResponse(String message, HttpStatus httpStatus) {
        ExceptionResponse response = new ExceptionResponse(message);
        log.error(response.toString());
        return ResponseEntity.status(httpStatus).body(response);
    }

}
