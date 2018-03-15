package ch.frostnova.mimic.api.provider;

import ch.frostnova.mimic.api.KeyValueStore;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link KeyValueStore}s.
 *
 * @author pwalser
 * @since 29.01.2018.
 */
public class KeyValueStoreTest {

    @Test
    public void testMemoryKeyValueStore() {
        test(new MemoryKeyValueStore());
    }

    private void test(KeyValueStore keyValueStore) {
        Assert.assertNotNull(keyValueStore);

        keyValueStore.clear();
        Assert.assertNotNull(keyValueStore.getKeys());
        Assert.assertTrue(keyValueStore.getKeys().isEmpty());

        keyValueStore.put("A", null);
        Assert.assertNotNull(keyValueStore.getKeys());
        Assert.assertTrue(keyValueStore.getKeys().isEmpty());

        keyValueStore.put("A", "123");
        Assert.assertNotNull(keyValueStore.getKeys());
        Assert.assertFalse(keyValueStore.getKeys().isEmpty());
        Assert.assertTrue(keyValueStore.getKeys().contains("A"));
        Assert.assertEquals(1, keyValueStore.getKeys().size());
        Assert.assertEquals("123", keyValueStore.get("A"));

        keyValueStore.put("B", "456");
        keyValueStore.put("C", "789");
        Assert.assertNotNull(keyValueStore.getKeys());
        Assert.assertFalse(keyValueStore.getKeys().isEmpty());
        Assert.assertTrue(keyValueStore.getKeys().contains("A"));
        Assert.assertTrue(keyValueStore.getKeys().contains("B"));
        Assert.assertTrue(keyValueStore.getKeys().contains("C"));
        Assert.assertEquals(3, keyValueStore.getKeys().size());
        Assert.assertEquals("123", keyValueStore.get("A"));
        Assert.assertEquals("456", keyValueStore.get("B"));
        Assert.assertEquals("789", keyValueStore.get("C"));

        keyValueStore.put("A", "Hello");
        Assert.assertNotNull(keyValueStore.getKeys());
        Assert.assertFalse(keyValueStore.getKeys().isEmpty());
        Assert.assertTrue(keyValueStore.getKeys().contains("A"));
        Assert.assertTrue(keyValueStore.getKeys().contains("B"));
        Assert.assertTrue(keyValueStore.getKeys().contains("C"));
        Assert.assertEquals(3, keyValueStore.getKeys().size());
        Assert.assertEquals("Hello", keyValueStore.get("A"));
        Assert.assertEquals("456", keyValueStore.get("B"));
        Assert.assertEquals("789", keyValueStore.get("C"));

        keyValueStore.put("B", null);
        keyValueStore.remove("C");
        Assert.assertNotNull(keyValueStore.getKeys());
        Assert.assertFalse(keyValueStore.getKeys().isEmpty());
        Assert.assertTrue(keyValueStore.getKeys().contains("A"));
        Assert.assertFalse(keyValueStore.getKeys().contains("B"));
        Assert.assertFalse(keyValueStore.getKeys().contains("B"));
        Assert.assertEquals(1, keyValueStore.getKeys().size());
        Assert.assertEquals("Hello", keyValueStore.get("A"));
        Assert.assertEquals(null, keyValueStore.get("B"));
        Assert.assertEquals(null, keyValueStore.get("C"));

        keyValueStore.put("B", "456");
        keyValueStore.put("C", "789");
        keyValueStore.clear();
        Assert.assertNotNull(keyValueStore.getKeys());
        Assert.assertTrue(keyValueStore.getKeys().isEmpty());
        Assert.assertEquals(null, keyValueStore.get("A"));
        Assert.assertEquals(null, keyValueStore.get("B"));
        Assert.assertEquals(null, keyValueStore.get("C"));
    }
}
