package ru.moniken.validation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// Обработка ошибок валидации клиентского JSON
@ControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        String message = Objects.requireNonNull(ex.getBindingResult().getFieldError())
                .getDefaultMessage();
        errors.put("message", message);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
