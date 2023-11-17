package ru.moniken.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.moniken.exception.RouteAlreadyExistsException;
import ru.moniken.exception.RouteNotFoundException;
import ru.moniken.model.entity.RouteEntity;
import ru.moniken.repository.RouteRepository;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteManagerService {

    final RouteRepository repository;

    /**
     * Нормализирует endpoint,
     * то есть заменяет последовательности символов "/" на символ,
     * добавляет символ "/" вначале
     *
     * @return нормализированный endpoint
     */
    public String normalizeEndpoint(String endpoint) {
        if (!endpoint.startsWith("/")) endpoint = "/" + endpoint;
        endpoint = endpoint.replaceAll("/+", "/");
        if (endpoint.endsWith("/")) endpoint = endpoint.substring(0, endpoint.length()-1);
        return endpoint;
    }

    private RouteEntity tryCommitRoute(RouteEntity route) {
        try {
            route.setEndpoint(normalizeEndpoint(route.getEndpoint()));
            return repository.save(route);
        } catch (DataIntegrityViolationException e) {
            throw new RouteAlreadyExistsException(route.getMethod().name(), route.getEndpoint());
        }
    }


    public RouteEntity create(RouteEntity route) {
        return tryCommitRoute(route);
    }

    public RouteEntity update(String id, RouteEntity route) {
        route.setId(id);
        return tryCommitRoute(route);
    }

    public List<RouteEntity> getAll() {
        return repository.findAll();
    }

    public RouteEntity getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException(id));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }
}
