package ch.frostnova.mimic.api.util;

import org.junit.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * JUnit assert utility methods
 *
 * @author pwalser
 * @since 25.01.2018
 */
public final class AssertUtils {

    private AssertUtils() {
        //
    }

    public static <T> void assertEquals(Set<? extends T> a, Set<? extends T> b) {
        Assert.assertTrue(checkEquals(a, b));
    }

    public static <T> void assertNotEquals(Set<? extends T> a, Set<? extends T> b) {
        Assert.assertFalse(checkEquals(a, b));
    }

    private static <T> boolean checkEquals(Set<? extends T> a, Set<? extends T> b) {

        if (a == b) {
            return true;
        }
        if (a == null && b != null || a != null && b == null) {
            return false;
        }
        if (a.size() != b.size()) {
            return false;
        }
        return a.containsAll(b) && b.containsAll(a);
    }

    public static <T> void assertEquals(List<? extends T> a, List<? extends T> b) {
        Assert.assertTrue(checkEquals(a, b));
    }

    public static <T> void assertNotEquals(List<? extends T> a, List<? extends T> b) {
        Assert.assertFalse(checkEquals(a, b));
    }

    private static <T> boolean checkEquals(List<? extends T> a, List<? extends T> b) {

        if (a == b) {
            return true;
        }
        if (a == null && b != null || a != null && b == null) {
            return false;
        }
        if (a.size() != b.size()) {
            return false;
        }

        Iterator<? extends T> ia = a.iterator();
        Iterator<? extends T> ib = b.iterator();
        while (ia.hasNext()) {
            if (!checkEquals(ia.next(), ib.next())) {
                return false;
            }
        }
        return true;
    }

    public static <K, V> void assertNotEquals(Map<K, ? extends V> a, Map<K, ? extends V> b) {
        Assert.assertFalse(checkEquals(a, b));
    }

    public static <K, V> void assertEquals(Map<K, ? extends V> a, Map<K, ? extends V> b) {
        Assert.assertTrue(checkEquals(a, b));
    }

    /**
     * Checks if the actual map is a superset of the expected map, meaning that all values of the expected map are
     * exactly contained in the actual map.
     *
     * @param expected expected key/values
     * @param actual   actual key/values (expected and optionally more)
     * @param <K>      key type
     * @param <V>      value type
     */
    public static <K, V> void assertSuperset(Map<K, ? extends V> expected, Map<K, ? extends V> actual) {
        Assert.assertTrue(checkSuperset(expected, actual));
    }

    private static <K, V> boolean checkEquals(Map<K, ? extends V> a, Map<K, ? extends V> b) {

        if (a == b) {
            return true;
        }
        if (a == null && b != null || a != null && b == null) {
            return false;
        }
        if (a.size() != b.size()) {
            return false;
        }
        Set<K> aKeys = a.keySet();
        Set<K> bKeys = b.keySet();
        if (!checkEquals(aKeys, bKeys)) {
            return false;
        }
        for (K key : aKeys) {
            if (!checkEquals(a.get(key), b.get(key))) {
                return false;
            }
        }
        return true;
    }

    private static <K, V> boolean checkSuperset(Map<K, ? extends V> expected, Map<K, ? extends V> actual) {

        if (expected == actual) {
            return true;
        }
        if (expected == null && actual != null || expected != null && actual == null) {
            return false;
        }
        if (expected.size() > actual.size()) {
            return false;
        }
        Set<K> aKeys = expected.keySet();
        Set<K> bKeys = actual.keySet();
        if (!bKeys.containsAll(aKeys)) {
            return false;
        }
        for (K key : aKeys) {
            if (!checkEquals(expected.get(key), actual.get(key))) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkEquals(Object a, Object b) {
        if (a instanceof Set && b instanceof Set) {
            return checkEquals((Set<?>) a, (Set<?>) b);
        }
        if (a instanceof List && b instanceof List) {
            return checkEquals((List<?>) a, (List<?>) b);
        }
        if (a instanceof Map && b instanceof Map) {
            return checkEquals(a, b);
        }
        return Objects.equals(a, b);
    }
}
