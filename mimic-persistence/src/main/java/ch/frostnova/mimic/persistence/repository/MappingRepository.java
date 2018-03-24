package ch.frostnova.mimic.persistence.repository;

import ch.frostnova.mimic.persistence.entity.MappingEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring JPA repository for Mimic Mappings
 */
public interface MappingRepository extends CrudRepository<MappingEntity, String> {
}
