package ru.moniken.dto.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpHeadersDeserializer extends JsonDeserializer<Map<String, String>> {
    @Override
    public Map<String, String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        Map<String, String> map = new HashMap<>();

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName().toLowerCase();
            jsonParser.nextToken();

            if (jsonParser.hasCurrentToken() && jsonParser.currentToken() == JsonToken.VALUE_STRING) {
                String value = jsonParser.getValueAsString();
                if (map.containsKey(fieldName)) {
                    value = map.get(fieldName) + ", " + value;
                }

                map.put(fieldName, value);
            }
        }

        return map;
    }
}
