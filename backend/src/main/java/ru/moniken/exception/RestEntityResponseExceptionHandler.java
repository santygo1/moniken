package ru.moniken.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestEntityResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ClientStatusException.class})
    public ResponseEntity<Object> handleClientStatusException(ClientStatusException exception) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", exception.getMessage());
        return new ResponseEntity<>(responseBody, exception.getStatus());
    }

}
