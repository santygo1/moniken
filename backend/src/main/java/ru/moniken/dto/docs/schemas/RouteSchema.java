package ru.moniken.dto.docs.schemas;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import ru.moniken.dto.RouteDTO;
import ru.moniken.model.records.HttpMethod;

import java.util.List;
import java.util.Map;

// !!! Схемы нужны для корректного отображения схем в springdocs,
// т.к нет интеграции с аннотацией jackson @JsonView
// Никакой практической значимостью они не обладают - нужны только для документации
public abstract class RouteSchema {

    @Schema(title = "Details Route Scheme", description = "Fullish route response")
    public static abstract class DetailsRoute extends RouteDTO {

        @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Route id")
        String id;

        @Schema(description = "Route name" +
                "<br><i>by default: </i><Endpoint's value>", maxLength = 30)
        String name;

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
                description = "Route endpoint for request." +
                "</br>Rules:<b></b>" +
                "<ul>" +
                "<li>Couple of method-endpoint must be uniqueit</li>" +
                "<li>Endpoint must starts with '/' and ends without '/'</li>" +
                "<li>Endpoint must not contains characters other than letters, numeric or '/'</li>" +
                "<li>Endpoint must not start with /moniken prefix</li>" +
                "</ul>" +
                "</br><i>Examples:</i>" +
                "<ul>" +
                "<li>/new - <b>ok</b></li>" +
                "<li>/new/1234 - <b>ok</b></li>" +
                "<li>/moniken/new - <i>fail</i></li>" +
                "<li>new - <i>fail</i></li>" +
                "<li>/mo?!# - <i>fail</i></li>" +
                "</ul>"
        )
        String endpoint;

        @Schema(nullable = true)
        Map<String, Object> body;

        @Schema(defaultValue = "GET", description = "Route response method" +
                "<br>Rules:" +
                "<ul>" +
                "<li>Couple of method-endpoint must be unique</li>" +
                "</ul>" +
                "<i>by default: </i>GET")
        HttpMethod method;

        @Schema( defaultValue = "OK",
                description = "Route response status: number or constant name for status" +
                "<br>Examples:" +
                "<ul>" +
                "<li>method_not_allowed</li>" +
                "<li>METHOD_NOT_ALLOWED</li>" +
                "<li>405</li>" +
                "</ul>" +
                "<i>by default: </i>200")
        HttpStatus status;

        @Schema(nullable = true)
        Map<String, String> headers;

        @Schema(nullable = true, defaultValue = "0",
                description = "Set the server response delay in milliseconds", minimum = "0", maximum = "60000")
        int timeout;

        @Schema(nullable = true, description = "Description has no effect on the server response, it's need for note", maxLength = 500)
        String description;

        @Hidden
        List<Link> links;
    }

    @Schema(title = "Short Route Scheme", description = "Short route response for presentation in list")
    public static abstract class ShortRoute extends DetailsRoute {
        @Hidden
        String description;

        @Hidden
        int timeout;

        @Hidden
        Map<String, String> body;

        @Hidden
        HttpMethod method;

        @Hidden
        HttpStatus status;

        @Hidden
        Map<String, String> headers;
    }

    @Schema(title = "Write Route Scheme", description = "Scheme for adding and updating route")
    public static abstract class WriteRoute extends DetailsRoute{

        @Hidden
        String id;
    }
}
