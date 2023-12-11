package ru.moniken.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.moniken.validation.WithoutSpaces;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouteCollectionDTO {

    @JsonView(Views.Short.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String id;

    @JsonView(Views.Short.class)
    @Length(max = 30, message = "{collection.error.name.max-length}")
    @WithoutSpaces(message = "{collection.error.name.without-spaces}")
    @NotBlank(message = "{collection.error.name.not-blank}")
    String name;

    @Length(max = 500, message = "{collection.error.description.max-length}")
    @JsonView(Views.Details.class)
    String description;
}
