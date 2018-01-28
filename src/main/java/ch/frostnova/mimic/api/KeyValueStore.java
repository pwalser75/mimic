package ch.frostnova.mimic.api;

/**
 * Key/Value store.
 *
 * @author pwalser
 * @since 28.01.2018.
 */
public interface KeyValueStore {

    void put(String key, String value);

    String get(String key);

    void clear(String key);
}
