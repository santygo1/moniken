package ru.moniken.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class LocalizedException extends RuntimeException {

    private final HttpStatus status;
    private final String messageSourcePath;
    private final Object[] messageSourceArguments;

    LocalizedException(HttpStatus status, String messageSourcePath, Object... messageSourceArguments) {
        this.status = status;
        this.messageSourcePath = messageSourcePath;
        this.messageSourceArguments = messageSourceArguments;
    }
}
