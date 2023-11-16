package ru.moniken.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.moniken.exception.NotFoundRouteException;
import ru.moniken.model.Route;
import ru.moniken.service.RouteManagerService;

import java.util.List;

// TODO: Добавить HATEOAS
// TODO: Добавить перевод
// TODO: Логирование

@RestController
@RequestMapping("/moniken/config") // TODO: добавить в конфиг
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteManagerController {

    final RouteManagerService routeService;

    final static String ID = "/{id}";

    @GetMapping
    ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAll());
    }

    @GetMapping(ID)
    ResponseEntity<Route> getRouteById(@PathVariable String id){
        return ResponseEntity.ok(routeService.getById(id)
                .orElseThrow(() -> new NotFoundRouteException(id)));
    }

    @PostMapping
    ResponseEntity<Route> createRoute(@RequestBody Route route) {
        return ResponseEntity.ok(routeService.create(route));
    }

    @PutMapping(ID)
    ResponseEntity<Route> updateRoute(
            @PathVariable String id,
            @RequestBody Route update){
        return ResponseEntity.ok(routeService.update(id, update));
    }

    @DeleteMapping(ID)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    void deleteRoute(@PathVariable String id){
        routeService.deleteById(id);
    }

}
