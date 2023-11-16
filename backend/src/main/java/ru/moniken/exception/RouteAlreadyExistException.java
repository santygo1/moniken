package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class RouteAlreadyExistException extends ClientStatusException {

    public RouteAlreadyExistException(String endpoint) {
        super(String.format("Route с конечной точкой %s уже существует", endpoint), HttpStatus.CONFLICT); // TODO: Перевод
    }
}
