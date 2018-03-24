package ch.frostnova.mimic.persistence.entity;

import ch.frostnova.mimic.api.type.RequestMethod;

import javax.persistence.*;

/**
 * Mimic Mapping Entity
 */
@Entity
@Table(name = "mapping")
public class MappingEntity extends BaseEntity {

    @Column(name = "display_name", length = 512, nullable = false)
    private String displayName;

    @Column(name = "description", length = 2048)
    private String description;

    @Column(name = "method", length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestMethod requestMethod;

    @Column(name = "path", length = 2048, nullable = false)
    private String path;

    @Lob
    @Column(name = "script", nullable = false)
    private String script;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
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

    @Override
    public String toString() {
        return getId() + ": " + getRequestMethod() + " " + getPath();
    }
}
