package ch.frostnova.mimic.api;

import ch.frostnova.mimic.api.type.RequestMethod;

import java.util.Map;

/**
 * Model for a web request (HTTP)
 *
 * @author pwalser
 * @since 23.01.2018.
 */
public interface WebRequest {

    /**
     * Binds a path mapping to the request (defines placeholders for path parameters, such as
     * <code>/{tenant}/resource/{id}</code>
     *
     * @param pathMapping path mapping, optional (if not set, no path parameters will be extracted).
     */
    void bind(String pathMapping);

    RequestMethod getMethod();

    String getPath();

    Map<String, String> getHeaders();

    Map<String, String> getPathParams();

    Map<String, String> getQueryParams();

    Map<String, String> getFormParams();

    String getBody();
}
