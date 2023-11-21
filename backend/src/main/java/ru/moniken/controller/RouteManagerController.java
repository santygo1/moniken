package ru.moniken.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.moniken.dto.RouteDTO;
import ru.moniken.dto.Views;
import ru.moniken.model.entity.Route;
import ru.moniken.service.RouteManagerService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// TODO: Написать тесты
// TODO: Протестировать в докере
// TODO: Export базы в файл
@RestController
@RequestMapping("${moniken.endpoint}/routes")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteManagerController {

    final RouteManagerService routeService;
    final ModelMapper mapper;
    final static String ID = "/{id}";

    @JsonView(Views.Short.class)
    @GetMapping
    ResponseEntity<Iterable<RouteDTO>> getAllRoutes() {
        Iterable<RouteDTO> routes = routeService.getAll().stream()
                .map((r) -> mapper.map(r, RouteDTO.class)
                        .add(defaultLinks(r.getId(), r.getCollection().getName())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(routes);
    }

    @JsonView(Views.Details.class)
    @GetMapping(ID)
    ResponseEntity<RouteDTO> getRouteById(@PathVariable String id) {
        Route route = routeService.getById(id);
        RouteDTO dto = mapper.map(route, RouteDTO.class)
                .add(defaultLinks(route.getId(), route.getCollection().getName()));

        return ResponseEntity.ok(dto);
    }

    @JsonView(Views.Details.class)
    @PutMapping(ID)
    ResponseEntity<RouteDTO> updateRoute(
            @PathVariable String id,
            @Valid @RequestBody RouteDTO update) {
        Route route = routeService.update(id, mapper.map(update, Route.class));

        RouteDTO dto = mapper.map(route, RouteDTO.class)
                .add(defaultLinks(route.getId(), route.getCollection().getName()));

        return ResponseEntity
                .ok(dto);
    }

    @DeleteMapping(ID)
    ResponseEntity<Void> deleteRoute(@PathVariable String id) {
        routeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public static Link selfLink(String routeId){
        return linkTo(methodOn(RouteManagerController.class).getRouteById(routeId))
                .withRel("self");
    }

    // Ссылки HATEOAS для роута, которые ссылаются на себя и на коллекцию, к которой относится роут
    public static List<Link> defaultLinks(String routeId, String collectionName) {
        return List.of(
                selfLink(routeId),
                linkTo(methodOn(RouteCollectionController.class).getCollectionByName(collectionName))
                        .withRel("collection")
                        .withName(collectionName)
        );
    }
}
