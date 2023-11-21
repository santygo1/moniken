package ru.moniken.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.moniken.config.MonikenConfig;
import ru.moniken.exception.GlobalUndefinedException;
import ru.moniken.exception.ReservedServiceEndpointException;
import ru.moniken.exception.RouteAlreadyExistsException;
import ru.moniken.exception.RouteNotFoundException;
import ru.moniken.model.entity.Route;
import ru.moniken.repository.RouteRepository;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteManagerService {

    final RouteRepository repository;

    final MonikenConfig monikenEndpoint;

    private void checkReservedEndpoint(String endpoint) {
        String reservedEndpoint = monikenEndpoint.getEndpoint();
        if (endpoint.startsWith(reservedEndpoint))
            throw new ReservedServiceEndpointException(reservedEndpoint);
    }

    private Route commitRouteOrExcept(Route route) {
        checkReservedEndpoint(route.getEndpoint()); // Проверка на допустимость endpoint'а

        if (route.getName() == null) {
            route.setName(route.getEndpoint());
        }

        try {
            return repository.save(route);
        } catch (DataIntegrityViolationException e) {
            if (e.getLocalizedMessage().contains("Unique index or primary key violation")) {
                throw new RouteAlreadyExistsException(route.getMethod().name(), route.getEndpoint());
            } else {
                throw new GlobalUndefinedException();
            }
        }
    }

    public Route create(Route route) {
        return commitRouteOrExcept(route);
    }

    public Route update(String id, Route update) {
        update.setId(id);

        Route route = getById(id);
        update.setCollection(route.getCollection());

        return commitRouteOrExcept(update);
    }

    public List<Route> getAll() {
        return repository.findAll();
    }

    public Route getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException(id));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
