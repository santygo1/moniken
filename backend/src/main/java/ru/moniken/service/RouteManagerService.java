package ru.moniken.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.moniken.exception.NotFoundRouteException;
import ru.moniken.exception.RouteAlreadyExistException;
import ru.moniken.model.Route;
import ru.moniken.repository.RouteRepository;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteManagerService {

    final RouteRepository repository;
    final MessageSource messageSource;

    private Route tryCommitRoute(Route route) {
        try {
            return repository.save(route);
        } catch (DataIntegrityViolationException e) {
            throw new RouteAlreadyExistException(
                    String.format(
                            messageSource.getMessage(
                                    "route.error.already-exist",
                                    null,
                                    LocaleContextHolder.getLocale()),
                            route.getEndpoint()
                    )
            );
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

    public Route getById(String id) {
        return repository.findById(id).orElseThrow(() ->
                new NotFoundRouteException(
                        String.format(
                                messageSource.getMessage(
                                        "route.error.not-found",
                                        null,
                                        LocaleContextHolder.getLocale()),
                                id
                        )
                )
        );
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }
}
