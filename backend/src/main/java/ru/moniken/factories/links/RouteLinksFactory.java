package ru.moniken.factories.links;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import ru.moniken.controller.RouteCollectionController;
import ru.moniken.controller.RouteManagerController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RouteLinksFactory extends ExpandedWithEndpointLinkFactory {

    public Link selfLink(String routeId) {
        return expandLink(linkTo(methodOn(RouteManagerController.class).getRouteById(routeId))
                .withRel("self"));
    }

    // Ссылки HATEOAS для роута, которые ссылаются на себя и на коллекцию, к которой относится роут
    public List<Link> defaultLinks(String routeId, String collectionName) {
        return List.of(
                selfLink(routeId),
                expandLink(linkTo(methodOn(RouteCollectionController.class).getCollectionByName(collectionName))
                        .withRel("collection")
                        .withName(collectionName))
        );
    }
}
