package ch.frostnova.mimic.persistence.repository;

import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.persistence.entity.MappingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Spring JPA repository for Mimic Mappings
 */
public interface MappingRepository extends CrudRepository<MappingEntity, String> {

    /**
     * Find matching mappings by request method
     *
     * @param requestMethod request method
     * @return matching mappings
     */
    List<MappingEntity> findByRequestMethod(RequestMethod requestMethod);
}
