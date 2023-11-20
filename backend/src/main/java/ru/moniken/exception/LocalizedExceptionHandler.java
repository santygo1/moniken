package ru.moniken.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// Обработчик ошибок с локализованным сообщением
@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocalizedExceptionHandler {

    MessageSource messageSource;

    @ExceptionHandler({LocalizedException.class})
    public ResponseEntity<Object> handleClientStatusException(LocalizedException exception) {
        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("message", String.format(
                messageSource.getMessage(
                        exception.getMessageSourcePath(),
                        null,
                        LocaleContextHolder.getLocale()),
                exception.getMessageSourceArguments()));

        return new ResponseEntity<>(responseBody, exception.getStatus());
    }

}
