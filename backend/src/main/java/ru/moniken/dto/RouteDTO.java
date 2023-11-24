package ru.moniken.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import ru.moniken.dto.deserializer.HttpHeadersDeserializer;
import ru.moniken.dto.deserializer.HttpMethodDeserializer;
import ru.moniken.dto.deserializer.HttpStatusDeserializer;
import ru.moniken.model.records.HttpMethod;
import ru.moniken.validation.Endpoint;

import java.util.HashMap;
import java.util.Map;

@Hidden
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouteDTO extends RepresentationModel<RouteDTO> {


    @JsonView(Views.Short.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String id;

    @JsonView(Views.Short.class)
    @Length(max = 30, message = "{route.error.name.max-length}")
    String name;

    @JsonView(Views.Short.class)
    @NotBlank(message = "{route.error.endpoint.not-blank}")
    @Endpoint(message = "{route.error.endpoint.endpoint-check}")
    String endpoint;

    @JsonView(Views.Details.class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Object> body = new HashMap<>();

    @JsonView(Views.Short.class)
    @JsonDeserialize(using = HttpMethodDeserializer.class)
    HttpMethod method = HttpMethod.GET;

    @JsonView(Views.Details.class)
    @JsonDeserialize(using = HttpStatusDeserializer.class)
    HttpStatus status = HttpStatus.OK;

    @JsonView(Views.Details.class)
    @JsonDeserialize(using = HttpHeadersDeserializer.class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, String> headers = new HashMap<>();

    @JsonView(Views.Details.class)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Max(message = "{route.error.timeout.max}", value = 60000)
    @Min(message = "{route.error.timeout.negative}", value = 0)
    int timeout = 0;

    @JsonView(Views.Details.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Length(max = 500, message = "{route.error.description.max-length}")
    String description;
}


