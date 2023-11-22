package ru.moniken.factories.links;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
class ExpandedWithEndpointLinkFactory {

    @Value(ExpandedWithEndpointLinkProxy.ENDPOINT)
    protected String monikenEndpoint;

    protected Link expandLink(Link notExpanded){
        return new ExpandedWithEndpointLinkProxy(notExpanded, monikenEndpoint);
    }
}
