package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class RouteStatusNotExistsException extends LocalizedException {
    public RouteStatusNotExistsException(String incorrectStatus) {
        super(HttpStatus.CONFLICT, "route.error.status.not-exists", incorrectStatus);
    }
}
