package ru.moniken.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import ru.moniken.validation.Endpoint;

@Validated
@Configuration
@ConfigurationProperties(prefix = "moniken")
@Getter
@Setter
public class MonikenConfig {

    @NotNull(message = "Describe moniken endpoint")
    @Endpoint(message = "Endpoint syntax error")
    private String endpoint;
}
