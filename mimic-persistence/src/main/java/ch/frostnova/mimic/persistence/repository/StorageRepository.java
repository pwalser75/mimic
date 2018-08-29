package ch.frostnova.mimic.persistence.repository;

import ch.frostnova.mimic.persistence.entity.StorageEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Spring JPA repository for Mimic Storage
 */
public interface StorageRepository extends CrudRepository<StorageEntity, String> {

    /**
     * Find an entry by its repository/resource id
     *
     * @param repositoryId repository id, required
     * @param resourceId   resource id, required
     * @return optional storage entity
     */
    Optional<StorageEntity> findByRepositoryIdAndResourceId(String repositoryId, String resourceId);
}
