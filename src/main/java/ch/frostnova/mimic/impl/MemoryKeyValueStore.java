package ch.frostnova.mimic.impl;

import ch.frostnova.mimic.api.KeyValueStore;
import ch.frostnova.util.check.Check;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory key/value store, stores runtime-scoped values (valid until restart).
 *
 * @author pwalser
 * @since 28.01.2018.
 */
public class MemoryKeyValueStore implements KeyValueStore {

    private final Map<String, String> store = new HashMap<>();

    @Override
    public void put(String key, String value) {
        Check.required("key", key);
        if (value == null) {
            store.remove(key);
        } else {
            store.put(key, value);
        }
    }

    @Override
    public String get(String key) {
        Check.required("key", key);
        return String.valueOf(store.get(key));
    }

    @Override
    public void clear(String key) {
        Check.required("key", key);
        store.remove(key);
    }
}
