package ru.moniken.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.moniken.model.entity.Route;
import ru.moniken.service.RouteProducerService;

import java.util.List;

@RestController
@RequestMapping
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteProducerController {

    final RouteProducerService routeService;

    @RequestMapping("**")
    ResponseEntity<Object> getRouteResponse(HttpServletRequest request) {
        String endpoint = request.getRequestURI();

        // Ищем роуты по конечной точке
        List<Route> routes = routeService.getByEndpoint(endpoint);

        // Если конечная точка не существует значит not found
        if (routes.size() == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        System.out.println(routes);

        Route route = routes.stream()
                // Если конечная точка существует, то ищем среди них метод
                .filter((r) -> request.getMethod().equals(r.getMethod().name()))
                .findAny()
                // Если конечная точка существует, но метод недоступен, значит not allowed
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED));

        HttpHeaders httpHeaders = new HttpHeaders();
        route.getHeaders().forEach(httpHeaders::set);

        int timeout = route.getTimeout();
        if (timeout > 0) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<>(route.getBody(), httpHeaders, route.getStatus());
    }
}
