package ru.moniken.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class OpenAPIConfig {

    private final MonikenConfig monikenConfig;

    @Value("${server.port}")
    String port;

    @Bean
    public OpenAPI openApiInformation(){
        Server devServer = new Server()
                .url("http://localhost:" + port)
                .description("Development Server URL");

        Contact contact = new Contact()
                .email("danilkaspirin@gmail.com")
                .name("Danil Spirin");

        Info info = new Info()
                .contact(contact)
                .title("Moniken API\uD83D\uDD03")
                .description("Simplify server mock for testing and development.\n" +
                        "Allows to create route with concrete endpoint, headers and response.")
                .version(monikenConfig.getVersion());

        return new OpenAPI().info(info).addServersItem(devServer);
    }

}
