package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class RouteAlreadyExistException extends ClientStatusException {

    public RouteAlreadyExistException(String msg) {
        super(msg, HttpStatus.CONFLICT);
    }
}
