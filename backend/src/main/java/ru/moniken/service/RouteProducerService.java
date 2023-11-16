package ru.moniken.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.moniken.model.Route;
import ru.moniken.repository.RouteRepository;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteProducerService {

    final RouteRepository repository;

    public Optional<Route> getByEndpoint(String endpoint) {
        return repository.findByEndpoint(endpoint);
    }
}
