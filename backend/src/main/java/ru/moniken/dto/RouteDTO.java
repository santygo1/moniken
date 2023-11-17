package ru.moniken.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import ru.moniken.dto.deserializer.HttpHeadersDeserializer;
import ru.moniken.dto.deserializer.HttpMethodDeserializer;
import ru.moniken.dto.deserializer.HttpStatusDeserializer;
import ru.moniken.model.records.HTTPMethod;
import ru.moniken.validation.Endpoint;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RouteDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String id;

    @NotBlank(message = "{route.error.endpoint.not-blank}")
    @Endpoint(message = "{route.error.endpoint.endpoint-check}")
    String endpoint;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Object> body = new HashMap<>();

    @JsonDeserialize(using = HttpMethodDeserializer.class)
    HTTPMethod method = HTTPMethod.GET;

    @JsonDeserialize(using = HttpStatusDeserializer.class)
    HttpStatus status = HttpStatus.OK;

    @JsonDeserialize(using = HttpHeadersDeserializer.class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, String> headers = new HashMap<>();

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Max(message = "{route.error.timeout.max}", value = 60000)
    @Min(message = "{route.error.timeout.negative}", value = 0)
    int timeout = 0;
}
