package ru.moniken.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.moniken.exception.GlobalUndefinedException;
import ru.moniken.exception.RouteCollectionAlreadyExistsException;
import ru.moniken.exception.RouteCollectionNotFoundException;
import ru.moniken.model.entity.Route;
import ru.moniken.model.entity.RouteCollection;
import ru.moniken.repository.RouteCollectionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteCollectionService {

    final RouteManagerService routeService;
    final RouteCollectionRepository collectionRepository;

    public List<RouteCollection> getAll() {
        return collectionRepository.findAll();
    }

    public RouteCollection getByName(String collectionName) {
        return collectionRepository.findByName(collectionName)
                .orElseThrow(() -> new RouteCollectionNotFoundException(collectionName));
    }

    // Пробует сохранить коллекцию, если коллекция с именем уже существует - бросает ошибку
    private RouteCollection commitOrExcept(RouteCollection collection) {
        try {
            return collectionRepository.save(collection);
        } catch (DataIntegrityViolationException e) {
            if (e.getLocalizedMessage().contains("Unique index or primary key violation")) {
                throw new RouteCollectionAlreadyExistsException(collection.getName());
            } else {
                throw new GlobalUndefinedException();
            }
        }
    }

    public RouteCollection create(RouteCollection collection) {
        return commitOrExcept(collection);
    }


    public RouteCollection update(String collectionName, RouteCollection update) {
        RouteCollection toBeUpdated = getByName(collectionName);
        toBeUpdated.setName(update.getName());
        toBeUpdated.setDescription(update.getDescription());

        return commitOrExcept(toBeUpdated);
    }

    public Route addRoute(String collectionId, Route route) {
        RouteCollection collection = getByName(collectionId);
        route.setCollection(collection);

        return routeService.create(route);
    }

    @Transactional
    public void delete(String collectionName) {
        collectionRepository.deleteByName(collectionName);
    }
}
