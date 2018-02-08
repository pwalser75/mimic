package ch.frostnova.mimic.servlet;

import ch.frostnova.mimic.api.KeyValueStore;
import ch.frostnova.mimic.api.WebRequest;
import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.api.type.TemplateExpression;
import ch.frostnova.mimic.impl.HttpSessionKeyValueStore;
import ch.frostnova.util.check.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link WebRequest} wrapper around a {@link HttpServletRequest}
 *
 * @author pwalser
 * @since 23.01.2018.
 */
public class ServletWebRequest implements WebRequest {

    private final static String CONTENT_TYPE_MULTIPART = "multipart/form-data";
    private final static String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";

    private static final Logger logger = LoggerFactory.getLogger(ServletWebRequest.class);

    private final HttpServletRequest request;
    private TemplateExpression pathMapping;
    private final KeyValueStore sessionKeyValueStore;

    private List<RequestPart> parts;
    private byte[] body;

    public ServletWebRequest(HttpServletRequest request) {
        this.request = Check.required(request, "request");
        sessionKeyValueStore = new HttpSessionKeyValueStore(request.getSession());
    }

    @Override
    public void bind(TemplateExpression pathMapping) {
        this.pathMapping = pathMapping;
    }

    @Override
    public KeyValueStore getSession() {
        return sessionKeyValueStore;
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
    public String getContentType() {
        return getHeaders().get("content-type");
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
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null) {
            parameterMap.forEach((k, v) -> {
                if (isQueryParam(k) && v.length > 0) {
                    result.put(k, v[0]);
                }
            });
        }
        return result;
    }

    private boolean isQueryParam(String paramName) {
        String queryString = request.getQueryString();
        if (queryString == null) {
            return false;
        }
        try {
            String encodedKey = URLEncoder.encode(paramName, StandardCharsets.UTF_8.displayName());
            return (queryString.startsWith(encodedKey + "=") || queryString.contains("&" + encodedKey + "="));
        } catch (UnsupportedEncodingException utf8unsupported) {
            // UTF-8 unsupported, really?
            return false;
        }
    }

    @Override
    public Map<String, String> getFormParams() {
        Map<String, String> result = new HashMap<>();

        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null) {
            parameterMap.forEach((k, v) -> {
                if (!isQueryParam(k) && v.length > 0) {
                    result.put(k, v[0]);
                }
            });
        }
        return result;
    }

    private boolean isMultiPart() {
        String contentType = getContentType();
        return contentType != null && contentType.startsWith(CONTENT_TYPE_MULTIPART);
    }

    private boolean isFormURLEncoded() {
        String contentType = getContentType();
        return contentType != null && contentType.startsWith(CONTENT_TYPE_FORM_URLENCODED);
    }

    @Override
    public byte[] getBody() {
        if (getContentType() == null) {
            return null;
        }
        if (isMultiPart() || isFormURLEncoded()) {
            return null;
        }
        if (body == null) {
            try (BufferedInputStream in = new BufferedInputStream(request.getInputStream())) {

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[0xFFF];
                int read;
                while ((read = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, read);
                }
                return out.toByteArray();

            } catch (IOException ex) {
                logger.error("Could not body", ex);
                throw new RuntimeException("Could not body: " + ex.getClass().getName() + ": " + ex.getMessage());
            }
        }
        return body;
    }

    @Override
    public List<RequestPart> getParts() {

        if (!isMultiPart()) {
            return Collections.emptyList();
        }
        if (parts == null) {
            parts = new ArrayList<>();
            try {
                for (Part part : request.getParts()) {
                    parts.add(new ServletRequestPart(part));
                }
            } catch (IOException | ServletException ex) {
                logger.error("Could not read parts", ex);
                throw new RuntimeException("Could not read parts: " + ex.getClass().getName() + ": " + ex.getMessage());
            }
        }
        return parts;
    }

    public static class ServletRequestPart implements RequestPart {

        private String contentType;
        private String name;
        private long size;
        private Map<String, String> headers = new HashMap<>();
        private byte[] content;

        public ServletRequestPart(Part part) {
            contentType = part.getContentType();
            name = part.getName();
            size = part.getSize();

            part.getHeaderNames().forEach(header -> {
                headers.put(header, part.getHeader(header));
            });

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (BufferedInputStream in = new BufferedInputStream(part.getInputStream())) {
                int read;
                while ((read = in.read()) > 0) {
                    buffer.write(read);
                }
            } catch (IOException ex) {
                logger.error("Could not read part data", ex);
                throw new RuntimeException("Could not read part data: " + ex.getClass().getName() + ": " + ex.getMessage());
            }
            content = buffer.toByteArray();
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public Map<String, String> getHeaders() {
            return headers;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getSize() {
            return size;
        }

        @Override
        public byte[] getContent() {
            return content;
        }
    }
}
