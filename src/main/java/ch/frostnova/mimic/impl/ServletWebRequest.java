package ch.frostnova.mimic.impl;

import ch.frostnova.mimic.api.WebRequest;
import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.api.type.TemplateExpression;
import ch.frostnova.util.check.Check;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link WebRequest} wrapper around a {@link HttpServletRequest}
 *
 * @author pwalser
 * @since 23.01.2018.
 */
public class ServletWebRequest implements WebRequest {

    private final HttpServletRequest request;
    private TemplateExpression pathMapping;

    @Override
    public void bind(TemplateExpression pathMapping) {
        this.pathMapping = pathMapping;
    }

    public ServletWebRequest(HttpServletRequest request) {
        this.request = Check.required(request, "request");
    }

    @Override
    public RequestMethod getMethod() {
        return RequestMethod.resolve(request.getMethod());
    }

    @Override
    public String getPath() {
        return request.getRequestURI();
    }

    @Override
    public Map<String, String> getHeaders() {

        Map<String, String> result = new HashMap<>();
        Collections.list(request.getHeaderNames()).forEach(header -> {
            result.put(header, Collections.list(request.getHeaders(header)).stream().collect(Collectors.joining(",")));
        });
        return result;
    }

    @Override
    public Map<String, String> getPathParams() {
        if (pathMapping == null) {
            return Collections.emptyMap();
        }
        Map<String, String> pathParams = pathMapping.getPlaceholderValues(getPath());
        return pathParams != null ? pathParams : Collections.emptyMap();
    }

    @Override
    public Map<String, String> getQueryParams() {
        Map<String, String> result = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> {
            if (isQueryParam(k) && v.length > 0) {
                result.put(k, v[0]);
            }
        });
        return result;
    }

    private boolean isQueryParam(String paramName) {
        String queryString = request.getQueryString();
        try {
            String encodedKey = URLEncoder.encode(paramName, StandardCharsets.UTF_8.displayName());
            System.out.println(queryString + "     " + encodedKey);
            return (queryString.startsWith(encodedKey + "=") || queryString.contains("&" + encodedKey + "="));
        } catch (UnsupportedEncodingException utf8unsupported) {
            // UTF-8 unsupported, really?
            return false;
        }
    }

    @Override
    public Map<String, String> getFormParams() {
        Map<String, String> result = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> {
            if (!isQueryParam(k) && v.length > 0) {
                result.put(k, v[0]);
            }
        });
        return result;
    }

    @Override
    public String getBody() {
        //TODO: implement

        return null;
//        throw new UnsupportedOperationException();
    }
}
