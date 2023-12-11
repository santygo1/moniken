package ru.moniken.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {


    private final MonikenConfig config;

    public CustomWebMvcConfigurer(MonikenConfig config) {
        this.config = config;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry
                .addViewController(config.getEndpoint() + "/ui")
                .setViewName("forward:" + config.getEndpoint() + "/index.html");
    }
}
