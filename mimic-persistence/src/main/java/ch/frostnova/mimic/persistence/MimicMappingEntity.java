package ch.frostnova.mimic.persistence;

import ch.frostnova.mimic.api.type.RequestMethod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Mimic Mapping Entity
 */
@Entity
@Table(name = "mapping")
public class MimicMappingEntity extends BaseEntity {


    @Column(name = "method", length = 16, nullable = false)
    private RequestMethod requestMethod;

    @Column(name = "path", length = 2048, nullable = false)
    private String path;

    @Lob
    @Column(name = "script", nullable = false)
    private String script;


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
