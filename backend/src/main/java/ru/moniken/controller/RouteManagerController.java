package ru.moniken.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.moniken.dto.RouteDTO;
import ru.moniken.exception.ReservedServiceEndpointException;
import ru.moniken.model.entity.RouteEntity;
import ru.moniken.service.RouteManagerService;

import java.net.URI;
import java.util.stream.Collectors;

// TODO: Подключить Swagger
// TODO: Добавить коллекции и подумать над HATEOAS
// TODO: Добавить логирование
// TODO: Добавить проверку нагрузки
// TODO: Добавить балансировшик нагрузки
@RestController
@RequestMapping("${moniken.config.endpoint}") // TODO: добавить в конфиг
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RouteManagerController {

    final RouteManagerService routeService;
    final ModelMapper modelMapper;

    @Value("${moniken.config.endpoint}")
    String controllerMapping;

    final static String ID = "/{id}";

    private void checkReserved(String endpoint){
        if (routeService.normalizeEndpoint(endpoint)
                .startsWith(routeService.normalizeEndpoint(controllerMapping)))
            throw new ReservedServiceEndpointException(controllerMapping);
    }

    @GetMapping
    ResponseEntity<Iterable<RouteDTO>> getAllRoutes() {
        Iterable<RouteDTO> routes = routeService.getAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(routes);
    }

    @GetMapping(ID)
    ResponseEntity<RouteDTO> getRouteById(@PathVariable String id) {
        return ResponseEntity
                .ok(convertToDto(routeService.getById(id)));
    }

    @PostMapping
    ResponseEntity<RouteDTO> createRoute(@Valid @RequestBody RouteDTO route) {
        checkReserved(route.getEndpoint());
        RouteDTO created = convertToDto(routeService.create(convertToEntity(route)));

        return ResponseEntity
                .created(URI.create(controllerMapping + "/" + route.getId()))
                .body(created);
    }

    @PutMapping(ID)
    ResponseEntity<RouteDTO> updateRoute(
            @PathVariable String id,
            @Valid @RequestBody RouteDTO update) {
        checkReserved(update.getEndpoint());

        if (routeService.existsById(id)) {
            return ResponseEntity
                    .ok(convertToDto(routeService.update(id, convertToEntity(update))));
        } else {
            RouteEntity created = routeService.create(convertToEntity(update));
            return ResponseEntity
                    .created(URI.create(controllerMapping + "/" + created.getId())) // Location
                    .body(convertToDto(created));
        }
    }

    @DeleteMapping(ID)
    ResponseEntity<Object> deleteRoute(@PathVariable String id) {
        routeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private RouteDTO convertToDto(RouteEntity entity) {
        return modelMapper.map(entity, RouteDTO.class);
    }

    private RouteEntity convertToEntity(RouteDTO dto) {
        RouteEntity mapping = modelMapper.map(dto, RouteEntity.class);
        System.out.println(mapping);
        return mapping;
    }
}
