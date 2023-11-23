package ru.moniken.dto;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

@Hidden
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouteCollectionDTO extends RepresentationModel<RouteCollectionDTO> {

    @JsonView(Views.Short.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String id;

    @JsonView(Views.Short.class)
    @Length(max = 30, message = "{collection.error.name.max-length}")
    @NotBlank(message = "{collection.error.name.not-blank}")
    String name;

    @Length(max = 500, message = "{collection.error.description.max-length}")
    @JsonView(Views.Details.class)
    String description;
}
