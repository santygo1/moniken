package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class NotFoundRouteException extends ClientStatusException {
    public NotFoundRouteException(String id) {
        super(String.format("Route с id %s не найден", id), HttpStatus.NOT_FOUND); // TODO: Перевод
    }
}
