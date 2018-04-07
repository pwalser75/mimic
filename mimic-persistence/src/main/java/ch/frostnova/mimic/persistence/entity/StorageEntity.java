package ch.frostnova.mimic.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Mimic Storage Entity
 */
@Entity
@Table(name = "storage")
public class StorageEntity extends BaseEntity {

    @Column(name = "repository_id", length = 64, nullable = false)
    private String repositoryId;

    @Column(name = "resource_id", length = 256, nullable = false)
    private String resourceId;

    @Column(name = "content_type", length = 64, nullable = false)
    private String contentType;

    @Column(name = "content_length", length = 256, nullable = false)
    private long contentLength;

    @Column(name = "content", nullable = false)
    private byte[] content;

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return getId() + ": " + getRepositoryId() + "/" + getResourceId();
    }
}
