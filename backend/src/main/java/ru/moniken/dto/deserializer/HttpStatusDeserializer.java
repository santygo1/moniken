package ru.moniken.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.http.HttpStatus;
import ru.moniken.exception.RouteStatusNotExistsException;

import java.io.IOException;

public class HttpStatusDeserializer extends JsonDeserializer<HttpStatus> {
    @Override
    public HttpStatus deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext)
            throws IOException {
        String result = jsonParser.getText();
        String upperResult = result.toUpperCase();
        HttpStatus status = null;

        try {
            // Проверяем является статус кодом или строкой
            boolean isStatusCode = result.matches("[0-9]{3}");
            if (isStatusCode) {
                status = HttpStatus.valueOf(Integer.parseInt(upperResult));
            } else {
                status = HttpStatus.valueOf(result);
            }
        } catch (IllegalArgumentException exception) {
            throw new RouteStatusNotExistsException(result);
        }

        return status;
    }

}
