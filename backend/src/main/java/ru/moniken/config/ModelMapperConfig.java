package ru.moniken.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.moniken.dto.RouteDTO;
import ru.moniken.model.entity.Route;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true);

        mapper.addMappings(new PropertyMap<Route, RouteDTO>() {
            @Override
            protected void configure() {
                // Преобразование объекта RouteCollection в название коллекции
                map().setCollection(source.getCollection().getName());
            }
        });

        mapper.addMappings(new PropertyMap<RouteDTO, Route>() {
            @Override
            protected void configure() {
                /*
                Пропускаем поле коллекции из DTO в Route потому что пользователь не может напрямую указывать
                коллекцию к которой относится роут
                 */
                skip(destination.getCollection());
            }
        });

        return mapper;
    }
}
