package ch.frostnova.mimic.api;

import java.util.Set;

/**
 * Key/Value store.
 *
 * @author pwalser
 * @since 28.01.2018.
 */
public interface KeyValueStore {

    /**
     * Set a value.
     *
     * @param key   key, required
     * @param value value, optional (if the value is null, it will be cleared).
     */
    void put(String key, String value);

    /**
     * Get a value
     *
     * @param key key, required
     * @return value (or null, if absent).
     */
    String get(String key);

    /**
     * Clears a value (or does nothing if the value is absent).
     *
     * @param key key, required
     */
    void remove(String key);

    /**
     * Returns the set of keys for which values are stored
     *
     * @return key set (may be empty, but never null)
     */
    Set<String> getKeys();

    /**
     * Clears the store, removing all values.
     */
    void clear();
}
