package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class GlobalUndefinedException extends LocalizedException{

    public GlobalUndefinedException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "global.error.undefined");
    }
}
