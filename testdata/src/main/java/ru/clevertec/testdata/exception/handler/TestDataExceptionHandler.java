package ru.clevertec.testdata.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.starter.exception.BlackListException;
import ru.clevertec.starter.exception.SessionAwareException;
import ru.clevertec.starter.exception.SessionServiceException;
import ru.clevertec.testdata.exception.NotFoundException;
import ru.clevertec.testdata.exception.ServiceException;
import ru.clevertec.testdata.exception.UniqueException;
import ru.clevertec.testdata.exception.model.ExceptionResponse;

@Slf4j
@ControllerAdvice
public class TestDataExceptionHandler {

    @ExceptionHandler(UniqueException.class)
    public ResponseEntity<ExceptionResponse> handleUniqueException(UniqueException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionResponse> handleServiceException(ServiceException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SessionAwareException.class)
    public ResponseEntity<ExceptionResponse> handleSessionAwareException(SessionAwareException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BlackListException.class)
    public ResponseEntity<ExceptionResponse> handleBlackListException(BlackListException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SessionServiceException.class)
    public ResponseEntity<ExceptionResponse> handleSessionServiceException(SessionServiceException exception) {
        return sendResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> sendResponse(String message, HttpStatus httpStatus) {
        ExceptionResponse response = new ExceptionResponse(message);
        log.error(response.toString());
        return ResponseEntity.status(httpStatus).body(response);
    }

}
