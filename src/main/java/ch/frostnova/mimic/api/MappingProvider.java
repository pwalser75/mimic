package ch.frostnova.mimic.api;

import java.util.Set;

/**
 * Interface for mapping providers.
 *
 * @author pwalser
 * @since 31.01.2018.
 */
public interface MappingProvider {

    /**
     * Return the list of mappings for this provider
     *
     * @return list of mappings, must not be null, but can be empty.
     */
    Set<MimicMapping> getMappings();
}
