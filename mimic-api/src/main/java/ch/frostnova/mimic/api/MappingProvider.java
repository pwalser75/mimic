package ch.frostnova.mimic.api;

import ch.frostnova.mimic.api.type.RequestMethod;

import java.util.Set;

/**
 * Strategy interface for mapping providers (provide mimic mapping definitions).
 *
 * @author pwalser
 * @since 31.01.2018.
 */
public interface MappingProvider {

    /**
     * Return mappings matching the given request method and path
     *
     * @param method request method, required
     * @param path   requested path, required
     * @return set of matching mappings, must not be null, but can be empty.
     */
    Set<MimicMapping> getMappings(RequestMethod method, String path);
}
