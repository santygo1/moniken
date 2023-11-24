package ru.moniken.dto.docs.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Simple Error Schema", description = "For internationalization message do request with" +
        " Header Accept-Language: (ru, en)")
public abstract class ErrorSchema {
    @Schema(description = "Error message", defaultValue = "error", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty() // Нужно для того, чтобы jackson создал прокси
    String message;

}
