package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class RouteMethodNotExistsException extends LocalizedException {
    public RouteMethodNotExistsException(String incorrectMethod) {
        super(HttpStatus.CONFLICT, "route.error.method.not-exists", incorrectMethod);
    }
}
