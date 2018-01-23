package ch.frostnova.mimic.servlet;

import ch.frostnova.mimic.api.WebRequest;
import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.util.check.Check;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * {@link WebRequest} wrapper around a {@link HttpServletRequest}
 *
 * @author pwalser
 * @since 23.01.2018.
 */
public class ServletWebRequest implements WebRequest {

    private final HttpServletRequest request;

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
        //TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getPathParams() {
        //TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getQueryParams() {
        //TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getFormParams() {
        //TODO: implement
        throw new UnsupportedOperationException();
    }

    @Override
    public String getBody() {
        //TODO: implement
        throw new UnsupportedOperationException();
    }
}
