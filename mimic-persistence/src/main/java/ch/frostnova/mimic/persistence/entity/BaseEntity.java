package ch.frostnova.mimic.persistence.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base Entity, uses a UUID id, and includes audit metadata (creation and last modification date).
 *
 * @author pwalser
 * @since 08.02.2018.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "generated-id")
    @GenericGenerator(name = "generated-id", strategy = "ch.frostnova.mimic.persistence.generator.IdGenerator")
    @Column(name = "id", length = 48, unique = true, nullable = false)
    private String id;

    @Version
    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at", nullable = false)
    private LocalDateTime lastModifiedAt;

    public String getId() {
        return id;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isPersistent() {
        return createdAt != null;
    }

    @PrePersist
    @PreUpdate
    private void setAuditDates() {
        lastModifiedAt = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = lastModifiedAt;
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !Objects.equals(getClass(), obj.getClass())) {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;
        return Objects.equals(id, other.id);
    }
}