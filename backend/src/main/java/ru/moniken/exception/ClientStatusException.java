package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class ClientStatusException extends RuntimeException {

    private final HttpStatus status;

    ClientStatusException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
