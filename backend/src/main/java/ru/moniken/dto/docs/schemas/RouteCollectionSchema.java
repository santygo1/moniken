package ru.moniken.dto.docs.schemas;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.Link;
import ru.moniken.dto.RouteCollectionDTO;

import java.util.List;

// !!! Схемы нужны для корректного отображения схем в springdocs,
// т.к нет интеграции с аннотацией jackson @JsonView
// Никакой практической значимостью они не обладают - нужны только для документации
public abstract class RouteCollectionSchema {

    @Schema(title = "Details Route Collection Scheme", description = "Fullish route collection response")
    public static abstract class DetailsCollection extends RouteCollectionDTO{

        @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Collection id")
        String id;


        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1,
                description = "Collection name", maxLength = 30)
        String name;

        @Schema(nullable = true, description = "Description for collection", maxLength = 500)
        String description;

        @Hidden
        List<Link> links;
    }

    @Schema(title = "Short Route Collection Scheme", description = "Short route collection response for presentation in list")
    public static abstract class ShortCollection extends DetailsCollection {
        @Hidden
        String description;
    }

    @Schema(title = "Write Collection Scheme", description = "Scheme for adding and updating collection")
    public static abstract class WriteCollection extends DetailsCollection{
        @Hidden
        String id;

    }
}
