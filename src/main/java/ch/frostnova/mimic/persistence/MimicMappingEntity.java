package ch.frostnova.mimic.persistence;

import ch.frostnova.mimic.api.type.RequestMethod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Mimic Mapping Entity
 */
@Entity
@Table(name = "mapping")
public class MimicMappingEntity {

    private static final long serialVersionUID = -1166461789114073327L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "method", length = 16, nullable = false)
    private RequestMethod requestMethod;


    @Column(name = "path", length = 2048, nullable = false)
    private String path;

    @Lob
    @Column(name = "script", nullable = false)
    private String script;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_modified_at", nullable = false)
    private LocalDateTime lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return getId() + ": " + getRequestMethod() + " " + getPath();
    }

    public boolean isPersistent() {
        return id != null;
    }

    @PrePersist
    private void setAuditDates() {
        lastModifiedDate = LocalDateTime.now();
        if (creationDate == null) {
            creationDate = lastModifiedDate;
        }
    }
}
