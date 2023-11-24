package ru.moniken.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Route Producer", description = "Producing created route, with specified configuration")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteProducerController {

    final RouteProducerService routeService;

    /**
     * Обрабатывает все запросы, которые не начинаются с endpoint указанного для moniken
     * P.S: Скорее всего это костыль, но другого решения я придумать не смог
     */
    static final String ALL_WITH_EXCLUDE_MONIKEN = "{name:^(?!\\" + "${moniken.endpoint}" + ").+}";

    @Operation(summary = "This method is produced created routes values")
    @RequestMapping(ALL_WITH_EXCLUDE_MONIKEN)
    ResponseEntity<Object> getRouteResponse(HttpServletRequest request) {
        String endpoint = request.getRequestURI();
        System.out.println(endpoint);

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
