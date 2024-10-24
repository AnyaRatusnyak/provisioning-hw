package com.voxloud.provisioning.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String STATUS_KEY = "status";
    private static final String ERRORS_KEY = "errors";

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<Object> handleDeviceNotFoundException(DeviceNotFoundException ex) {
        Map<String, Object> body = createErrorBody(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<Object> handleJsonParseException(JsonParseException ex) {
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonConversionException.class)
    public ResponseEntity<Object> handleJsonConversionException(JsonConversionException ex) {
        Map<String, Object> body = createErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        Map<String, Object> body = createErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createErrorBody(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP_KEY, LocalDateTime.now());
        body.put(STATUS_KEY, status.value());
        body.put(ERRORS_KEY, message);
        return body;
    }
}
