package ru.moniken.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.moniken.exception.RouteMethodNotExistsException;
import ru.moniken.model.records.HttpMethod;

import java.io.IOException;

public class HttpMethodDeserializer extends JsonDeserializer<HttpMethod> {
    @Override
    public HttpMethod deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext)
            throws IOException {
        String result = jsonParser.getText();
        String upperResult = result.toUpperCase();
        HttpMethod method;

        try {
            method = HttpMethod.valueOf(upperResult);
        } catch (IllegalArgumentException e) {
            throw new RouteMethodNotExistsException(result);
        }

        return method;
    }
}
