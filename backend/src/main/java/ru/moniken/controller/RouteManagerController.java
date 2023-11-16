package ru.moniken.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.moniken.model.Route;
import ru.moniken.service.RouteManagerService;

import java.net.URI;
import java.util.List;

// TODO: Добавить HATEOAS
// TODO: Подключить Swagger
// TODO: Добавить логирование
// TODO: Добавить верификацию JSON

@RestController
@RequestMapping("/moniken/config") // TODO: добавить в конфиг
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteManagerController {

    final RouteManagerService routeService;
    final String controllerMapping = this.getClass().getAnnotation(RequestMapping.class).value()[0];

    final static String ID = "/{id}";

    @GetMapping
    ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAll());
    }

    @GetMapping(ID)
    ResponseEntity<Route> getRouteById(@PathVariable String id) {
        return ResponseEntity.ok(routeService.getById(id));
    }

    @PostMapping
    ResponseEntity<Route> createRoute(@RequestBody Route route) {
        Route created = routeService.create(route);

        return ResponseEntity
                .created(URI.create(controllerMapping + "/" + route.getId())) // TODO: изменить при конфиге
                .body(created);
    }

    @PutMapping(ID)
    ResponseEntity<Route> updateRoute(
            @PathVariable String id,
            @RequestBody Route update) {

        return routeService.existsById(id) ?
                ResponseEntity.ok(routeService.update(id, update)) : createRoute(update);
    }

    @DeleteMapping(ID)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    void deleteRoute(@PathVariable String id) {
        routeService.deleteById(id);
    }

}
