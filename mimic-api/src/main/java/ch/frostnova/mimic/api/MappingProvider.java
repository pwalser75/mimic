package ch.frostnova.mimic.api;

import ch.frostnova.mimic.api.type.RequestMethod;

import java.util.Set;

/**
 * Interface for mapping providers.
 *
 * @author pwalser
 * @since 31.01.2018.
 */
public interface MappingProvider {

    /**
     * Return mappings matching the given request method and path
     *
     * @return set of matching mappings, must not be null, but can be empty.
     */
    Set<MimicMapping> getMappings(RequestMethod method, String path);
}
