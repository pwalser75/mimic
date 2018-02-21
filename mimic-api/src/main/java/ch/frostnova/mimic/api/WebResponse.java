package ch.frostnova.mimic.api;

import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckNumber;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Model for a web response (HTTP)
 *
 * @author pwalser
 * @since 23.01.2018.
 */
public final class WebResponse {

    private int status;
    private String contentType;
    private byte[] body;
    private Map<String, String> headers = new HashMap<>();

    public WebResponse() {
        this(200, "text/plain", "Mimic default response");
    }

    public WebResponse(int status, String contentType, byte[] body) {
        this.status = status;
        this.contentType = contentType;
        this.body = body;
    }

    public WebResponse(int status, String contentType, String body) {
        this.status = status;
        this.contentType = contentType;
        this.body = body != null ? body.getBytes(StandardCharsets.UTF_8) : null;
    }

    public static WebResponse error(int statusCode, String text) {
        Check.required(statusCode, "statusCode", CheckNumber.min(400), CheckNumber.max(599));
        return new WebResponse(statusCode, "text/plain", text);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setBody(String body) {
        this.body = body != null ? body.getBytes(StandardCharsets.UTF_8) : null;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeader(String key, String value) {
        Check.required(key, "header key");
        headers.put(key, value);
    }
}
