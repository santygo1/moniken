package ru.moniken.controller;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.moniken.dto.RouteCollectionDTO;
import ru.moniken.dto.RouteDTO;
import ru.moniken.dto.Views;
import ru.moniken.model.entity.Route;
import ru.moniken.model.entity.RouteCollection;
import ru.moniken.service.RouteCollectionService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("${moniken.endpoint}/collections")
public class RouteCollectionController {

    final RouteCollectionService collectionService;
    final ModelMapper mapper;

    @JsonView(Views.Short.class)
    @GetMapping
    ResponseEntity<Iterable<RouteCollectionDTO>> getAllCollections() {
        Iterable<RouteCollectionDTO> collectionDTOS =
                collectionService.getAll().stream()
                        // Преобразует коллекцию в дто и добавляет HATEOAS ссылку на себя
                        .map(collection ->
                                mapper.map(collection, RouteCollectionDTO.class)
                                        .add(selfLink(collection.getName()))
                        )
                        .collect(Collectors.toList());

        return ResponseEntity.ok(collectionDTOS);
    }

    @JsonView(Views.Details.class)
    @GetMapping("/{collectionName}")
    ResponseEntity<RouteCollectionDTO> getCollectionByName(@PathVariable String collectionName) {
        RouteCollection collection = collectionService.getByName(collectionName);

        RouteCollectionDTO dto = mapper.map(collection, RouteCollectionDTO.class)
                .add(defaultLinks(collection.getName()));

        return ResponseEntity.ok(dto);
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
        RouteCollectionDTO dto = mapper.map(created, RouteCollectionDTO.class)
                .add(defaultLinks(created.getName()));

        return ResponseEntity
                .created(URI.create(location))
                .body(dto);
    }

    @JsonView(Views.Details.class)
    @PostMapping("/{collectionName}/routes")
    ResponseEntity<RouteDTO> addRouteToCollection(
            @PathVariable String collectionName,
            @RequestBody @Valid RouteDTO routeDTO,
            HttpServletRequest request) {
        Route route = collectionService.addRoute(collectionName, mapper.map(routeDTO, Route.class));

        // Формируем данные для ответа
        String location = getCreatedLocation(request, route.getId());
        RouteDTO dto = mapper.map(route, RouteDTO.class)
                .add(RouteManagerController.defaultLinks(route.getId(), route.getCollection().getName()));

        return ResponseEntity
                .created(URI.create(location))
                .body(dto);
    }

    @JsonView(Views.Short.class)
    @GetMapping("/{collectionName}/routes")
    ResponseEntity<Iterable<RouteDTO>> getCollectionRoutes(@PathVariable String collectionName) {
        // Получаем роуты из коллекции и преобразуем в DTO
        Iterable<RouteDTO> routes = collectionService.getByName(collectionName).getRoutes().stream()
                .map(r -> mapper.map(r, RouteDTO.class)
                        .add(RouteManagerController.selfLink(r.getId())))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(routes);
    }

    @JsonView(Views.Details.class)
    @PutMapping("/{collectionName}")
    ResponseEntity<RouteCollectionDTO> updateCollection(@PathVariable String collectionName,
                                                        @RequestBody @Valid RouteCollectionDTO update) {
        RouteCollection updated = collectionService.update(collectionName, mapper.map(update, RouteCollection.class));

        RouteCollectionDTO dto = mapper.map(updated, RouteCollectionDTO.class)
                .add(defaultLinks(updated.getName()));

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{collectionName}")
    ResponseEntity<Void> deleteRouteCollection(@PathVariable String collectionName) {
        collectionService.delete(collectionName);
        return ResponseEntity.noContent().build();
    }

    // Возвращает HATEOAS ссылку на коллекцию
    public static Link selfLink(String collectionName) {
        return linkTo(methodOn(RouteCollectionController.class).getCollectionByName(collectionName))
                .withRel("self");
    }

    // Возвращает HATEOAS ссылки на коллекцию и роуты коллекций
    public static List<Link> defaultLinks(String collectionName) {
        return List.of(
                selfLink(collectionName),
                linkTo(methodOn(RouteCollectionController.class).getCollectionRoutes(collectionName))
                        .withRel("routes")
        );
    }
}
