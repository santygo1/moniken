package ru.moniken.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.moniken.dto.RouteDTO;
import ru.moniken.dto.Views;
import ru.moniken.model.entity.Route;
import ru.moniken.service.RouteManagerService;

import java.util.stream.Collectors;

// TODO: Подключить Swagger
// TODO: Добавить логирование
// TODO: Написать тесты
// TODO: Протестировать в докере
// TODO: Export базы в файл
@RestController
@RequestMapping("/routes")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteManagerController {

    final RouteManagerService routeService;
    final ModelMapper mapper;
    final static String ID = "/{id}";

    @JsonView(RouteDTO.SpecialView.Short.class)
    @GetMapping
    ResponseEntity<Iterable<RouteDTO>> getAllRoutes() {
        Iterable<RouteDTO> routes = routeService.getAll().stream()
                .map((r) -> mapper.map(r, RouteDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(routes);
    }

    @JsonView(RouteDTO.SpecialView.Details.class)
    @GetMapping(ID)
    ResponseEntity<RouteDTO> getRouteById(@PathVariable String id) {
        Route route = routeService.getById(id);

        return ResponseEntity
                .ok(mapper.map(route, RouteDTO.class));
    }

    @JsonView(Views.Details.class)
    @PutMapping(ID)
    ResponseEntity<RouteDTO> updateRoute(
            @PathVariable String id,
            @Valid @RequestBody RouteDTO update) {
        Route route = routeService.update(id, mapper.map(update, Route.class));

        return ResponseEntity
                .ok(mapper.map(route, RouteDTO.class));
    }

    @DeleteMapping(ID)
    ResponseEntity<Void> deleteRoute(@PathVariable String id) {
        routeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
