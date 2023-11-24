package ru.moniken.factories.links;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import ru.moniken.controller.RouteCollectionController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteCollectionLinksFactory extends ExpandedWithEndpointLinkFactory {


    // Возвращает HATEOAS ссылку на коллекцию
    public Link selfLink(String collectionName) {
        return expandLink(linkTo(methodOn(RouteCollectionController.class).getCollectionByName(collectionName))
                .withRel("self"));
    }

    // Возвращает HATEOAS ссылки на коллекцию и роуты коллекций
    public List<Link> defaultLinks(String collectionName) {
        return List.of(
                selfLink(collectionName),
                expandLink(linkTo(methodOn(RouteCollectionController.class).getCollectionRoutes(collectionName))
                        .withRel("routes"))
        );
    }
}
