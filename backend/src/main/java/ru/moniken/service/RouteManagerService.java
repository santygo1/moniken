package ru.moniken.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.moniken.exception.RouteAlreadyExistException;
import ru.moniken.model.Route;
import ru.moniken.repository.RouteRepository;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteManagerService {

    final RouteRepository repository;

    private Route tryCommitRoute(Route route){
        try {
            return repository.save(route);
        } catch (DataIntegrityViolationException e) {
            throw new RouteAlreadyExistException(route.getEndpoint());
        }
    }

    public Route create(Route route) {
        return tryCommitRoute(route);
    }

    public Route update(String id, Route route) {
        route.setId(id);
        return tryCommitRoute(route);
    }

    public List<Route> getAll() {
        return repository.findAll();
    }

    public Optional<Route> getById(String id) {
        return repository.findById(id);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
