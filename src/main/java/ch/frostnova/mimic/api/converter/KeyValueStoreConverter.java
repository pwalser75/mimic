package ch.frostnova.mimic.api.converter;

import ch.frostnova.mimic.api.KeyValueStore;
import ch.frostnova.mimic.impl.MemoryKeyValueStore;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;

/**
 * JAXB adapter for {@link KeyValueStore}
 */
public class KeyValueStoreConverter extends XmlAdapter<Map, KeyValueStore> {

    @Override
    public Map marshal(KeyValueStore value) throws Exception {
        if (value == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String key : value.getKeys()) {
            result.put(key, value.get(key));
        }
        return result;
    }

    @Override
    public KeyValueStore unmarshal(Map value) throws Exception {
        if (value == null) {
            return null;
        }
        KeyValueStore result = new MemoryKeyValueStore();
        for (Object key : value.keySet()) {
            Object val = value.get(key);
            if (val != null) {
                result.put(key.toString(), val.toString());
            }
        }
        return result;
    }
}
