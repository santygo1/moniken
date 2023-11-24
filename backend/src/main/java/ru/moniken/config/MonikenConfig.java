package ru.moniken.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import ru.moniken.validation.Endpoint;

@Validated
@Configuration
@ConfigurationProperties(prefix = "moniken")
public class MonikenConfig {

    @Getter
    @Setter
    @Length(min = 2, message = "Endpoint can't be empty")
    @NotNull(message = "Provide moniken endpoint")
    @Endpoint
    private String endpoint;

    @Getter
    @Setter
    private String version;

    @Getter
    private boolean welcomeConsole = true;

    public void setWelcomeConsole(String welcomeConsole) {
        this.welcomeConsole = Boolean.parseBoolean(welcomeConsole);
    }
}
