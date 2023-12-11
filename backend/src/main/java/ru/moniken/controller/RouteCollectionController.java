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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.moniken.dto.RouteCollectionDTO;
import ru.moniken.dto.RouteDTO;
import ru.moniken.dto.Views;
import ru.moniken.model.entity.Route;
import ru.moniken.model.entity.RouteCollection;
import ru.moniken.service.RouteCollectionService;

import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("${moniken.endpoint}/collections")
public class RouteCollectionController {

    final RouteCollectionService collectionService;
    final ModelMapper mapper;

    @JsonView(Views.Short.class)
    @GetMapping
    public ResponseEntity<Iterable<RouteCollectionDTO>> getAllCollections() {
        Iterable<RouteCollectionDTO> collectionDTOS =
                collectionService.getAll().stream()
                        .map(c -> mapper.map(c, RouteCollectionDTO.class))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(collectionDTOS);
    }

    @JsonView(Views.Details.class)
    @GetMapping("/{collectionName}")
    public ResponseEntity<RouteCollectionDTO> getCollectionByName(
            @PathVariable String collectionName) {
        RouteCollection collection = collectionService.getByName(collectionName);

        RouteCollectionDTO dto = mapper.map(collection, RouteCollectionDTO.class);

        return ResponseEntity.ok(dto);
    }

    /*  Создает путь для нового ресурса,
        основываясь на пути метода который создает ресурс(source)
        и названии нового ресурса */
    private URI getCreatedLocation(String newResourceName) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newResourceName)
                .toUri();
    }


    @JsonView(Views.Details.class)
    @PostMapping
    public ResponseEntity<RouteCollectionDTO> createCollection(
            @RequestBody @Valid RouteCollectionDTO collection) {

        RouteCollection created = collectionService.create(mapper.map(collection, RouteCollection.class));
        RouteCollectionDTO dto = mapper.map(created, RouteCollectionDTO.class);

        return ResponseEntity
                .created(getCreatedLocation(created.getName()))
                .body(dto);
    }

    @JsonView(Views.Details.class)
    @PostMapping("/{collectionName}/routes")
    public ResponseEntity<RouteDTO> addRouteToCollection(
            @PathVariable String collectionName,
            @RequestBody @Valid RouteDTO route) {

        Route routeModel = collectionService.addRoute(collectionName, mapper.map(route, Route.class));
        RouteDTO dto = mapper.map(routeModel, RouteDTO.class);

        return ResponseEntity
                .created(getCreatedLocation(routeModel.getId()))
                .body(dto);
    }


    @JsonView(Views.Short.class)
    @GetMapping("/{collectionName}/routes")
    public ResponseEntity<Iterable<RouteDTO>> getCollectionRoutes(
            @PathVariable String collectionName) {
        Iterable<RouteDTO> routes = collectionService.getByName(collectionName).getRoutes().stream()
                .map(r -> mapper.map(r, RouteDTO.class))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(routes);
    }

    @JsonView(Views.Details.class)
    @PutMapping("/{collectionName}")
    public ResponseEntity<RouteCollectionDTO> updateCollection(
            @PathVariable String collectionName,
            @RequestBody @Valid RouteCollectionDTO update) {
        RouteCollection updated = collectionService.update(collectionName, mapper.map(update, RouteCollection.class));

        RouteCollectionDTO dto = mapper.map(updated, RouteCollectionDTO.class);

        return ResponseEntity.ok(dto);
    }


    @DeleteMapping("/{collectionName}")
    public ResponseEntity<Void> deleteRouteCollection(
            @PathVariable String collectionName) {
        collectionService.delete(collectionName);
        return ResponseEntity.noContent().build();
    }
}
