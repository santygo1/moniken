package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class RouteNotFoundException extends LocalizedException {
    public RouteNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND, "route.error.not-found", id);
    }
}
