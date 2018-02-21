package ch.frostnova.mimic.persistence;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring JPA repository for Mimic Mappings
 */
public interface MimicMappingRepository extends CrudRepository<MimicMappingEntity, String> {
}
