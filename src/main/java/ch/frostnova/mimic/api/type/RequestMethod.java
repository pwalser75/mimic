package ch.frostnova.mimic.api.type;

import ch.frostnova.util.check.Check;

import java.util.NoSuchElementException;
import java.util.Optional;
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
    TRACE,
    PATCH,
    COPY,
    LINK,
    UNLINK,
    PURGE,
    LOCK,
    UNLOCK,
    DEBUG,
    VIEW;

    /**
     * Resolve a request method by (case insensitive) name.
     *
     * @param methodName method name
     * @return matching method (optional)
     */
    public static Optional<RequestMethod> resolve(final String methodName) {
        Check.required(methodName, "methodName");
        return Stream.of(RequestMethod.values())
                .filter(v -> v.name().equalsIgnoreCase(methodName))
                .findFirst();
    }

    /**
     * Resolve a request method by (case insensitive) name. If no matching request method is found, a NoSuchElementException is thrown.
     *
     * @param methodName method name
     * @return matching method
     * @throws NoSuchElementException when no matching request method was found.
     */
    public static RequestMethod checked(final String methodName) {
        return resolve(methodName)
                .orElseThrow(() -> new NoSuchElementException("no such request method: " + methodName));
    }
}