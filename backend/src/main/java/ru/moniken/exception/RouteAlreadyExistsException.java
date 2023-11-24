package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class RouteAlreadyExistsException extends LocalizedException {

    public RouteAlreadyExistsException(String method, String endpoint) {
        super(HttpStatus.CONFLICT, "route.error.already-exist", method, endpoint);
    }

}
