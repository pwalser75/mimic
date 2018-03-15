package ch.frostnova.mimic.api.converter;

import ch.frostnova.mimic.api.KeyValueStore;
import ch.frostnova.mimic.api.provider.MemoryKeyValueStore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

/**
 * JSON converter for  {@link KeyValueStore}
 */
public class KeyValueStoreConverter {

    public static class Serializer extends JsonSerializer<KeyValueStore> {

        @Override
        public void serialize(KeyValueStore keyValueStore, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            if (keyValueStore != null) {
                for (String key : keyValueStore.getKeys()) {
                    jsonGenerator.writeStringField(key, keyValueStore.get(key));
                }
                jsonGenerator.writeEndObject();
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<KeyValueStore> {

        @Override
        public KeyValueStore deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            KeyValueStore keyValueStore = new MemoryKeyValueStore();
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            node.fieldNames().forEachRemaining(field -> {
                JsonNode value = node.get(field);
                keyValueStore.put(field, value != null ? value.toString() : null);
            });
            return keyValueStore;
        }
    }
}
