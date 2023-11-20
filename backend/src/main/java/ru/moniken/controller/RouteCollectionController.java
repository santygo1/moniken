package ru.moniken.controller;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.moniken.dto.RouteCollectionDTO;
import ru.moniken.dto.RouteDTO;
import ru.moniken.dto.Views;
import ru.moniken.model.entity.Route;
import ru.moniken.model.entity.RouteCollection;
import ru.moniken.service.RouteCollectionService;

import java.net.URI;
import java.util.stream.Collectors;

// TODO: Добавить HATEOAS
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/collections")
public class RouteCollectionController {

    final RouteCollectionService collectionService;
    final ModelMapper mapper;

    @JsonView(Views.Short.class)
    @GetMapping
    ResponseEntity<Iterable<RouteCollectionDTO>> getAllCollections() {
        Iterable<RouteCollectionDTO> collectionDTOS =
                collectionService.getAll().stream()
                        .map(collection -> mapper.map(collection, RouteCollectionDTO.class))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(collectionDTOS);
    }

    @JsonView(Views.Details.class)
    @GetMapping("/{collectionName}")
    ResponseEntity<RouteCollectionDTO> getCollectionById(@PathVariable String collectionName) {
        return ResponseEntity.ok(mapper.map(collectionService.getByName(collectionName), RouteCollectionDTO.class));
    }

    /*  Создает путь для нового ресурса,
        основываясь на пути метода который создает ресурс(source)
        и названии нового ресурса */
    private String getCreatedLocation(HttpServletRequest source, String newResourceName) {
        return source.getRequestURI() + "/" + newResourceName;
    }

    @JsonView(Views.Details.class)
    @PostMapping
    ResponseEntity<RouteCollectionDTO> createCollection(
            @RequestBody @Valid RouteCollectionDTO collection,
            HttpServletRequest request) {
        RouteCollection created = collectionService.create(mapper.map(collection, RouteCollection.class));

        String location = getCreatedLocation(request, created.getName());

        return ResponseEntity
                .created(URI.create(location))
                .body(mapper.map(created, RouteCollectionDTO.class));
    }

    @JsonView(Views.Details.class)
    @PostMapping("/{collectionName}/routes")
    ResponseEntity<RouteDTO> addRouteToCollection(
            @PathVariable String collectionName,
            @RequestBody @Valid RouteDTO routeDTO,
            HttpServletRequest request) {
        Route route = collectionService.addRoute(collectionName, mapper.map(routeDTO, Route.class));
        String location = getCreatedLocation(request, route.getId());

        return ResponseEntity
                .created(URI.create(location))
                .body(mapper.map(route, RouteDTO.class));
    }

    @JsonView(Views.Short.class)
    @GetMapping("/{collectionName}/routes")
    ResponseEntity<Iterable<RouteDTO>> getCollectionRoutes(@PathVariable String collectionName) {
        // Получаем роуты из коллекции и преобразуем в DTO
        Iterable<RouteDTO> routes = collectionService.getByName(collectionName).getRoutes().stream()
                .map(r -> mapper.map(r, RouteDTO.class))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(routes);
    }

    @DeleteMapping("/{collectionName}")
    ResponseEntity<Void> deleteRouteCollection(@PathVariable String collectionName) {
        collectionService.delete(collectionName);
        return ResponseEntity.noContent().build();
    }
}
