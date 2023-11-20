package ru.moniken.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteCollectionDTO {

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
