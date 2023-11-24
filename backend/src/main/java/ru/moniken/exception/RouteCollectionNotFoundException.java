package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class RouteCollectionNotFoundException extends LocalizedException{

    public RouteCollectionNotFoundException(String collectionName) {
        super(HttpStatus.NOT_FOUND, "collection.error.not-found", collectionName);
    }
}
