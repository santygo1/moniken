package ru.moniken.exception;

import org.springframework.http.HttpStatus;

public class RouteCollectionAlreadyExistsException extends LocalizedException{

    public RouteCollectionAlreadyExistsException( String collectionName) {
        super(HttpStatus.CONFLICT, "collection.error.already-exist", collectionName);
    }
}
