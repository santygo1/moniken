package ru.moniken.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.moniken.dto.RouteDTO;
import ru.moniken.dto.Views;
import ru.moniken.dto.docs.schemas.ErrorSchema;
import ru.moniken.dto.docs.schemas.RouteSchema;
import ru.moniken.factories.links.RouteLinksFactory;
import ru.moniken.model.entity.Route;
import ru.moniken.service.RouteManagerService;

import java.util.stream.Collectors;

// TODO: Написать тесты
// TODO: Протестировать в докере
@Tag(name = "Routes", description = "Route management: get, update and delete routes")
@RestController
@RequestMapping("${moniken.endpoint}/routes")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteManagerController {

    final RouteManagerService routeService;
    final ModelMapper mapper;
    final RouteLinksFactory linksFactory;

    final static String ID = "/{id}";

    @Operation(summary = "Get all routes", ignoreJsonView = true)
    @ApiResponse(responseCode = "200",
            description = "Returning all created routes",
            content = {@Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = RouteSchema.ShortRoute.class))
            )},
            links = {
                    @io.swagger.v3.oas.annotations.links.Link(
                            name = "{collectionName}",
                            ref = "/collections/{collectionName}",
                            description = "Link to the collection that the route is associated with"),
                    @io.swagger.v3.oas.annotations.links.Link(
                            name = "self",
                            ref = "/routes/{routeID}"
                    )
            }
    )
    @JsonView(Views.Short.class)
    @GetMapping
    public ResponseEntity<Iterable<RouteDTO>> getAllRoutes() {
        Iterable<RouteDTO> routes = routeService.getAll().stream()
                .map((r) -> mapper.map(r, RouteDTO.class)
                        .add(linksFactory.defaultLinks(r.getId(), r.getCollection().getName())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(routes);
    }

    @Operation(summary = "Get route by id", ignoreJsonView = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Route with id was found",
                    content = {@Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = RouteSchema.DetailsRoute.class))},
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
            @ApiResponse(responseCode = "404",
                    description = "Route with id isn't founded",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorSchema.class)
                    )}
            )
    })
    @JsonView(Views.Details.class)
    @GetMapping(ID)
    public ResponseEntity<RouteDTO> getRouteById(
            @Parameter(description = "Route id for get")
            @PathVariable String id) {
        Route route = routeService.getById(id);
        RouteDTO dto = mapper.map(route, RouteDTO.class)
                .add(linksFactory.defaultLinks(route.getId(), route.getCollection().getName()));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Full update route by id",
            description = "*If some of field is filled in, it's replaced with null",
            ignoreJsonView = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Route successfully updated",
                    content = {@Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = RouteSchema.DetailsRoute.class)
                    )},
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
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorSchema.class)
                    )}
            ),
            @ApiResponse(responseCode = "404",
                    description = "Route with id isn't founded",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorSchema.class)
                    )}
            )
    })
    @Parameter(in = ParameterIn.QUERY,
            name = "update",
            required = true,
            description = "Full update for route. Non described field will be null",
            schema = @Schema(implementation = RouteSchema.WriteRoute.class))
    @JsonView(Views.Details.class)
    @PutMapping(ID)
    public ResponseEntity<RouteDTO> updateRoute(
            @Parameter(description = "Updated route id")
            @PathVariable String id,
            @Valid @RequestBody RouteDTO update) {
        Route route = routeService.update(id, mapper.map(update, Route.class));

        RouteDTO dto = mapper.map(route, RouteDTO.class)
                .add(linksFactory.defaultLinks(route.getId(), route.getCollection().getName()));

        return ResponseEntity
                .ok(dto);
    }

    @Operation(summary = "Delete route by id")
    @ApiResponse(responseCode = "204", description = "Route successfully deleted")
    @DeleteMapping(ID)
    public ResponseEntity<Void> deleteRoute(
            @Parameter(description = "Deleted route id")
            @PathVariable String id) {
        routeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
