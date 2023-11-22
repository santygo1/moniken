package ru.moniken.factories.links;

import org.springframework.hateoas.Link;

class ExpandedWithEndpointLinkProxy extends Link {

    public static final String ENDPOINT ="${moniken.endpoint}";

    public ExpandedWithEndpointLinkProxy(Link link, String prefixEndpoint){
        super(replaceEndpoint(link.getHref(), prefixEndpoint), link.getRel());
    }

    private static String replaceEndpoint(String href, String endpoint){
        return href.replace(ENDPOINT, endpoint);
    }
}
