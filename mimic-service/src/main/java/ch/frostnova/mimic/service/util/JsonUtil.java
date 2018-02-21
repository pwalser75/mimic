package ch.frostnova.mimic.service.util;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

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
     * @param <T>  type
     * @return json json
     */
    public static <T> String stringify(T value) {
        if (value == null) {
            return null;
        }
        try {
            StringWriter buffer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();

            mapper.setAnnotationIntrospector(
                    AnnotationIntrospector.pair(
                            new JacksonAnnotationIntrospector(),
                            new JaxbAnnotationIntrospector(mapper.getTypeFactory())
                    )
            );
            mapper.writeValue(buffer, value);
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
            ObjectMapper mapper = new ObjectMapper();

            mapper.setAnnotationIntrospector(
                    AnnotationIntrospector.pair(
                            new JacksonAnnotationIntrospector(),
                            new JaxbAnnotationIntrospector(mapper.getTypeFactory())
                    )
            );
            return mapper.readValue(json.getBytes(StandardCharsets.UTF_8), type);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
