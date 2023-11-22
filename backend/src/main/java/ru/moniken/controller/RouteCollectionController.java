package ru.moniken.controller;


import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.moniken.factories.links.RouteCollectionLinksFactory;
import ru.moniken.factories.links.RouteLinksFactory;
import ru.moniken.model.entity.Route;
import ru.moniken.model.entity.RouteCollection;
import ru.moniken.service.RouteCollectionService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Tag(name = "Collections",
        description = "Route collection management: " +
                "creating collections and collection routes," +
                " getting, updating and deleting collections")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("${moniken.endpoint}/collections")
public class RouteCollectionController {

    final RouteCollectionService collectionService;
    final ModelMapper mapper;
    final RouteCollectionLinksFactory collectionLinksFactory;
    final RouteLinksFactory routeLinksFactory;

    @Operation(summary = "Get all route collections")
    @ApiResponse(responseCode = "200",
            description = "Returning all created route collections",
            content = {@Content(mediaType = "application/hal+json", schema = @Schema(implementation = List.class))},
            links = {
                    @io.swagger.v3.oas.annotations.links.Link(
                            name = "self",
                            ref = "/collections/{collectionName}",
                            description = "Self route collection link"
                    )
            }
    )
    @JsonView(Views.Short.class)
    @GetMapping
    public ResponseEntity<Iterable<RouteCollectionDTO>> getAllCollections() {
        Iterable<RouteCollectionDTO> collectionDTOS =
                collectionService.getAll().stream()
                        // Преобразует коллекцию в дто и добавляет HATEOAS ссылку на себя
                        .map(collection ->
                                mapper.map(collection, RouteCollectionDTO.class)
                                        .add(collectionLinksFactory.selfLink(collection.getName()))
                        )
                        .collect(Collectors.toList());

        return ResponseEntity.ok(collectionDTOS);
    }

    @Operation(summary = "Get created collection by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Route collection with name was found",
                    content = {@Content(mediaType = "application/hal+json", schema = @Schema(implementation = RouteCollectionDTO.class))},
                    links = {
                            @io.swagger.v3.oas.annotations.links.Link(
                                    name = "routes",
                                    ref = "/collections/{collectionName}/routes",
                                    description = "Link to the collection routes"),
                            @io.swagger.v3.oas.annotations.links.Link(
                                    name = "self",
                                    ref = "/collections/{collectionName}",
                                    description = "Self route collection link"
                            )
                    }
            ),

            @ApiResponse(responseCode = "404",
                    description = "Route collection with name isn't founded",
                    content = {@Content(mediaType = "application/hal+json")}
            )
    })
    @JsonView(Views.Details.class)
    @GetMapping("/{collectionName}")
    public ResponseEntity<RouteCollectionDTO> getCollectionByName(@PathVariable String collectionName) {
        RouteCollection collection = collectionService.getByName(collectionName);

        RouteCollectionDTO dto = mapper.map(collection, RouteCollectionDTO.class)
                .add(collectionLinksFactory.defaultLinks(collection.getName()));

        return ResponseEntity.ok(dto);
    }

    /*  Создает путь для нового ресурса,
        основываясь на пути метода который создает ресурс(source)
        и названии нового ресурса */
    private String getCreatedLocation(HttpServletRequest source, String newResourceName) {
        return source.getRequestURI() + "/" + newResourceName;
    }


    @Operation(summary = "Create new route collection")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Route collection successfully create",
                    content = {@Content(mediaType = "application/hal+json")},
                    links = {
                            @io.swagger.v3.oas.annotations.links.Link(
                                    name = "routes",
                                    ref = "/collections/{collectionName}/routes",
                                    description = "Link to the collection routes"),
                            @io.swagger.v3.oas.annotations.links.Link(
                                    name = "self",
                                    ref = "/collections/{collectionName}",
                                    description = "Self route collection link"
                            )
                    },
                    headers = {@Header(name = "Location", description = "Created collection location")}
            ),
            @ApiResponse(responseCode = "409",
                    description = "Route collection with specified name already exists",
                    content = {@Content(mediaType = "application/hal+json")}
            )
    })
    @JsonView(Views.Details.class)
    @PostMapping
    public ResponseEntity<RouteCollectionDTO> createCollection(
            @RequestBody @Valid RouteCollectionDTO collection,
            HttpServletRequest request) {
        RouteCollection created = collectionService.create(mapper.map(collection, RouteCollection.class));

        String location = getCreatedLocation(request, created.getName());
        RouteCollectionDTO dto = mapper.map(created, RouteCollectionDTO.class)
                .add(collectionLinksFactory.defaultLinks(created.getName()));

        return ResponseEntity
                .created(URI.create(location))
                .body(dto);
    }

    @Operation(summary = "Create and add route to collection")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Route successfully created and chains with collection",
                    content = {@Content(mediaType = "application/hal+json")},
                    links = {
                            @io.swagger.v3.oas.annotations.links.Link(
                                    name = "{collectionName}",
                                    ref = "/collections/{collectionName}",
                                    description = "Link to the collection that the route is associated with"),
                            @io.swagger.v3.oas.annotations.links.Link(
                                    name = "self",
                                    ref = "/routes/{routeID}",
                                    description = "Self route link"
                            )
                    }
            ),
            @ApiResponse(responseCode = "409",
                    description = "Route with specified method and endpoint already exists",
                    content = {@Content(mediaType = "application/hal+json")}
            ),
            @ApiResponse(responseCode = "404",
                    description = "Collection with name isn't founded",
                    content = {@Content(mediaType = "application/hal+json")}
            )
    })
    @JsonView(Views.Details.class)
    @PostMapping("/{collectionName}/routes")
    public ResponseEntity<RouteDTO> addRouteToCollection(
            @PathVariable String collectionName,
            @RequestBody @Valid RouteDTO routeDTO,
            HttpServletRequest request) {
        Route route = collectionService.addRoute(collectionName, mapper.map(routeDTO, Route.class));

        // Формируем данные для ответа
        String location = getCreatedLocation(request, route.getId());
        RouteDTO dto = mapper.map(route, RouteDTO.class)
                .add(routeLinksFactory.defaultLinks(route.getId(), route.getCollection().getName()));

        return ResponseEntity
                .created(URI.create(location))
                .body(dto);
    }


    @Operation(summary = "Get collection routes")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Returning all collection routes",
                    content = {@Content(mediaType = "application/hal+json")},
                    links = {
                            @io.swagger.v3.oas.annotations.links.Link(
                                    name = "self",
                                    ref = "/routes/{routeId}",
                                    description = "Self route link"
                            )
                    }
            ),
            @ApiResponse(responseCode = "404",
                    description = "Route collection with specified name not found",
                    content = {@Content(mediaType = "application/hal+json")}
            )
    })
    @JsonView(Views.Short.class)
    @GetMapping("/{collectionName}/routes")
    public ResponseEntity<Iterable<RouteDTO>> getCollectionRoutes(@PathVariable String collectionName) {
        // Получаем роуты из коллекции и преобразуем в DTO
        Iterable<RouteDTO> routes = collectionService.getByName(collectionName).getRoutes().stream()
                .map(r -> mapper.map(r, RouteDTO.class)
                        .add(routeLinksFactory.selfLink(r.getId())))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(routes);
    }

    @Operation(summary = "Full update route collection by name",
            description = "*If some of field is filled in, it's replaced with null")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Route collection successfully updated",
                    content = {@Content(mediaType = "application/hal+json")},
                    links = {
                            @io.swagger.v3.oas.annotations.links.Link(
                                    name = "routes",
                                    ref = "/collections/{collectionName}/routes",
                                    description = "Link to the collection routes"),
                            @io.swagger.v3.oas.annotations.links.Link(
                                    name = "self",
                                    ref = "/collections/{collectionName}",
                                    description = "Self route collection link"
                            )
                    }),
            @ApiResponse(responseCode = "409",
                    description = "Route collection with specified name already exists",
                    content = {@Content(mediaType = "application/hal+json")}
            ),
            @ApiResponse(responseCode = "404",
                    description = "Route collection with name isn't founded",
                    content = {@Content(mediaType = "application/hal+json")}
            )
    })
    @JsonView(Views.Details.class)
    @PutMapping("/{collectionName}")
    public ResponseEntity<RouteCollectionDTO> updateCollection(@PathVariable String collectionName,
                                                        @RequestBody @Valid RouteCollectionDTO update) {
        RouteCollection updated = collectionService.update(collectionName, mapper.map(update, RouteCollection.class));

        RouteCollectionDTO dto = mapper.map(updated, RouteCollectionDTO.class)
                .add(collectionLinksFactory.defaultLinks(updated.getName()));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Delete route by id")
    @ApiResponse(responseCode = "204", description = "Route collection successfully deleted")
    @DeleteMapping("/{collectionName}")
    public ResponseEntity<Void> deleteRouteCollection(@PathVariable String collectionName) {
        collectionService.delete(collectionName);
        return ResponseEntity.noContent().build();
    }
}
