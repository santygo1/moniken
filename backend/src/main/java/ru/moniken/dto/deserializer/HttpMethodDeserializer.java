package ru.moniken.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.moniken.exception.RouteMethodNotExistsException;
import ru.moniken.model.records.HTTPMethod;

import java.io.IOException;

public class HttpMethodDeserializer extends JsonDeserializer<HTTPMethod> {
    @Override
    public HTTPMethod deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext)
            throws IOException {
        String result = jsonParser.getText();
        String upperResult = result.toUpperCase();
        HTTPMethod method;

        try {
            method = HTTPMethod.valueOf(upperResult);
        } catch (IllegalArgumentException e) {
            throw new RouteMethodNotExistsException(result);
        }

        return method;
    }
}
