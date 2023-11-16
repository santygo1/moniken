package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class NotFoundRouteException extends ClientStatusException {
    public NotFoundRouteException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }
}
