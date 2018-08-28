package ch.frostnova.mimic.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * JSON utility functions
 *
 * @author pwalser
 * @since 23.01.2018
 */
public final class JsonUtil {

    private JsonUtil() {

    }

    /**
     * Convert the given object to JSON using JAXB.
     *
     * @param value value
     * @param <T>   type
     * @return json json
     */
    public static <T> String stringify(T value) {
        if (value == null) {
            return null;
        }
        try {
            StringWriter buffer = new StringWriter();
            objectMapper().writeValue(buffer, value);
            return buffer.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Convert the given JSON back to an object using JAXB.
     *
     * @param type type
     * @param json json
     * @param <T>  type
     * @return object of the given type
     */
    public static <T> T parse(Class<T> type, String json) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper().readValue(json.getBytes(StandardCharsets.UTF_8), type);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());
        return mapper;
    }
}
