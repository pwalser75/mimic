package ch.frostnova.mimic.api.type;

import ch.frostnova.util.check.Check;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * HTTP verbs / request methods
 *
 * @author pwalser
 * @since 23.01.2018.
 */
public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    OPTIONS,
    TRACE;

    public static RequestMethod resolve(final String methodName) {
        Check.required(methodName, "methodName");
        return Stream.of(RequestMethod.values())
                .filter(v -> v.name().equalsIgnoreCase(methodName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("no such request method: " + methodName));
    }
}