package ch.frostnova.mimic.persistence.repository;

import ch.frostnova.mimic.persistence.entity.StorageEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring JPA repository for Mimic Storage
 */
public interface StorageRepository extends CrudRepository<StorageEntity, String> {

    /**
     * Find an entry by its repository/resource id
     *
     * @param repositoryId repository id, required
     * @param resourceId   resource id, required
     * @return storage entity, or null if not found
     */
    StorageEntity findByRepositoryIdAndResourceId(String repositoryId, String resourceId);
}
