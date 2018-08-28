package ch.frostnova.mimic.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Optional;

/**
 * A static resource.
 *
 * @author pwalser
 * @since 28.08.2018
 */
public class StaticResource {

    @JsonProperty("id")
    private String id;

    @JsonProperty("repositoryId")
    private String repositoryId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("contentType")
    private String contentType;

    @JsonProperty("content")
    private byte[] content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @JsonProperty(value = "size", access = JsonProperty.Access.READ_ONLY)
    public long getSize() {
        return content != null ? content.length : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaticResource that = (StaticResource) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s (%s/%s, %d bytes)",
                Optional.ofNullable(id).orElse("new"),
                Optional.ofNullable(repositoryId).orElse("?"),
                Optional.ofNullable(name).orElse("?"),
                getSize());
    }
}
