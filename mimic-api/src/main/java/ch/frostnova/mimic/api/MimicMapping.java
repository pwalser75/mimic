package ch.frostnova.mimic.api;

import ch.frostnova.mimic.api.converter.LocalDateTimeConverter;
import ch.frostnova.mimic.api.type.RequestMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

/**
 * A mimic mapping, with method/path mapping and javascript script (ECMASCRIPT 5.1) for processing requests.
 *
 * @author pwalser
 * @since 23.01.2018.
 */
public class MimicMapping {

    @JsonProperty("id")
    private String id;

    @JsonProperty("createdAt")
    @JsonSerialize(using = LocalDateTimeConverter.Serializer.class)
    @JsonDeserialize(using = LocalDateTimeConverter.Deserializer.class)
    private LocalDateTime createdAt;

    @JsonProperty("lastModifiedAt")
    @JsonSerialize(using = LocalDateTimeConverter.Serializer.class)
    @JsonDeserialize(using = LocalDateTimeConverter.Deserializer.class)
    private LocalDateTime lastModifiedAt;

    @JsonProperty("method")
    private RequestMethod method;

    @JsonProperty("path")
    private String path;

    @JsonProperty("script")
    private String script;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
