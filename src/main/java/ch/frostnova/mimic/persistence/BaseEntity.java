package ch.frostnova.mimic.persistence;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

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
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "last_modified_at", nullable = false)
    private ZonedDateTime lastModifiedAt;

    public BaseEntity() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public ZonedDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(ZonedDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPersistent() {
        return createdAt != null;
    }

    @PrePersist
    private void setAuditDates() {
        lastModifiedAt = ZonedDateTime.now();
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