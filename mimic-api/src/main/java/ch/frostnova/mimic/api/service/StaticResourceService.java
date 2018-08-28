package ch.frostnova.mimic.api.service;

import ch.frostnova.mimic.api.StaticResource;

/**
 * Service for storing and loading static resources
 *
 * @author pwalser
 * @since 28.08.2018
 */
public interface StaticResourceService {

    /**
     * Get a static resource, identified by the repository id and name
     *
     * @param repositoryId repository id, required
     * @param name         name, required
     * @return resource, or null if no such resource is found.
     */
    StaticResource get(String repositoryId, String name);

    /**
     * Load static resource, identified by the resource id
     *
     * @param resourceId resource id, required
     * @return resource, or null if no such resource is found.
     */
    StaticResource load(String resourceId);

    /**
     * Save (create or update) a static resource
     *
     * @param resource resource to save, required
     * @return persisted resource
     */
    StaticResource save(StaticResource resource);

    /**
     * Delete a resource by id. If no such resource is found, no exception is thrown (resource regarded as already deleted).
     *
     * @param resourceId resource id, required
     */
    void delete(String resourceId);
}
