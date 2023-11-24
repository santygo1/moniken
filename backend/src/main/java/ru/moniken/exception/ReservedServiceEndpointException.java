package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class ReservedServiceEndpointException extends LocalizedException{

    public ReservedServiceEndpointException(String endpoint) {
        super(HttpStatus.CONFLICT, "route.error.endpoint.reserved", endpoint);
    }
}
