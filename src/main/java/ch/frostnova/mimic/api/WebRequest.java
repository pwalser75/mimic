package ch.frostnova.mimic.api;

import ch.frostnova.mimic.api.type.RequestMethod;

import java.util.Map;

/**
 * @author pwalser
 * @since 23.01.2018.
 */
public interface WebRequest {

    RequestMethod getMethod();

    String getPath();

    Map<String, String> getHeaders();

    Map<String, String> getPathParams();

    Map<String, String> getQueryParams();

    Map<String, String> getFormParams();

    String getBody();
}
