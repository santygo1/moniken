package ru.moniken.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.moniken.model.Route;
import ru.moniken.service.RouteProducerService;

@RestController
@RequestMapping
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteProducerController {

    final RouteProducerService routeService;

    @GetMapping("**")
    ResponseEntity<Object> getRouteResponse(HttpServletRequest request) {
        String endpoint = request.getRequestURI().substring(1);
        Route route = routeService.getByEndpoint(endpoint)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(route.getBody());
    }
}
