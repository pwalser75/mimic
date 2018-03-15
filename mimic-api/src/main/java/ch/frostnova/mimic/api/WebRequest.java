package ch.frostnova.mimic.api;

import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.api.type.TemplateExpression;
import ch.frostnova.mimic.api.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

/**
 * Model for a web request (HTTP)
 *
 * @author pwalser
 * @since 23.01.2018.
 */
@XmlRootElement
public interface WebRequest {

    /**
     * Binds a path mapping to the request (defines placeholders for path parameters, such as
     * <code>/{tenant}/resource/{id}</code>
     *
     * @param pathMapping path mapping, optional (if not set, no path parameters will be extracted).
     */
    void bind(TemplateExpression pathMapping);

    @JsonProperty("session")
    KeyValueStore getSession();

    @JsonProperty("method")
    RequestMethod getMethod();

    @JsonProperty("path")
    String getPath();

    @JsonProperty("headers")
    Map<String, String> getHeaders();

    @JsonProperty("contentType")
    String getContentType();

    @JsonProperty("pathParams")
    Map<String, String> getPathParams();

    @JsonProperty("queryParams")
    Map<String, String> getQueryParams();

    @JsonProperty("formParams")
    Map<String, String> getFormParams();

    @JsonProperty("parts")
    List<RequestPart> getParts();

    @JsonProperty("body")
    byte[] getBody();

    @JsonProperty("userAgent")
    default UserAgent getUserAgent() {
        String userAgentHeader = getHeaders().get("user-agent");
        return userAgentHeader != null ? new UserAgent(userAgentHeader) : null;
    }

    /**
     * JSON conversion
     *
     * @return request as JSON string
     */
    @JsonIgnore
    default String toJSON() {
        return JsonUtil.stringify(this);
    }

    interface RequestPart {

        @JsonProperty("contentType")
        String getContentType();

        @JsonProperty("headers")
        Map<String, String> getHeaders();

        @JsonProperty("name")
        String getName();

        @JsonProperty("size")
        long getSize();

        @JsonProperty("content")
        byte[] getContent();
    }
}
