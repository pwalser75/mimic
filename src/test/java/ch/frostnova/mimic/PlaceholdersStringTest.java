package ch.frostnova.mimic;

import ch.frostnova.mimic.util.PlaceholderUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Tests for strings with placeholders (such as "abc{placeholder}def").
 *
 * @author pwalser
 * @since 24.01.2018.
 */
public class PlaceholdersStringTest {

    @Test
    public void testNoPlaceholders() {

        for (String s : Arrays.asList(null, "", " ", "What", "No placeholder {", "} still no { match")) {
            Set<String> placeholders = PlaceholderUtil.getPlaceholderKeys(s);
            Assert.assertNotNull(placeholders);
            Assert.assertTrue(placeholders.isEmpty());
        }
    }

    @Test
    public void testPlaceholders() {

        for (String s : Arrays.asList("{foo}", " {foo} ", "abc{foo}def", "Now {foo} twice:{foo}!")) {
            Set<String> placeholders = PlaceholderUtil.getPlaceholderKeys(s);
            Assert.assertNotNull(placeholders);
            Assert.assertEquals(1, placeholders.size());
            Assert.assertTrue(placeholders.contains("foo"));
        }

        BiConsumer<String, List<String>> test = (string, expectedKeys) -> {
            Set<String> placeholders = PlaceholderUtil.getPlaceholderKeys(string);
            Assert.assertNotNull(placeholders);
            Assert.assertEquals(expectedKeys.size(), placeholders.size());
            expectedKeys.forEach(key -> Assert.assertTrue(key + " is missing", placeholders.contains(key)));
        };

        test.accept("{Hello}, {World}!", Arrays.asList("Hello", "World"));
        test.accept("{Hello World}", Arrays.asList("Hello World"));
        test.accept("{{{curly} }}", Arrays.asList("{{curly"));
        test.accept("{a}, b {{c}} {}", Arrays.asList("a", "{c"));
    }

    @Test
    public void testExtractPlaceholders() {

        String template = "The {what} today{ something with whitespaces } {how}!";
        String actual = "The weather today is indeed terrific!";
        Assert.assertTrue(PlaceholderUtil.matches(template, actual));

        Map<String, String> map = PlaceholderUtil.getPlaceholderValues(template, actual);
        Assert.assertNotNull(map);
        Assert.assertEquals(3, map.size());
        Assert.assertEquals("weather", map.get("what"));
        Assert.assertEquals(" is indeed", map.get(" something with whitespaces "));
        Assert.assertEquals("terrific", map.get("how"));
    }

    @Test
    public void testExtractPlaceholdersNoMatch() {

        String template = "The {what} today was {how}";
        String actual = "The weather today is terrific";
        Assert.assertFalse(PlaceholderUtil.matches(template, actual));
        Assert.assertNull(PlaceholderUtil.getPlaceholderValues(template, actual));
    }

    @Test
    public void testExtractDuplicatePlaceholders() {

        String template = "{person} bought a {what} in the {what} store {when}";
        String actual = "Jessie bought a pet rock in the pet rock store today";
        Assert.assertTrue(PlaceholderUtil.matches(template, actual));
        Map<String, String> map = PlaceholderUtil.getPlaceholderValues(template, actual);

        Assert.assertNotNull(map);
        Assert.assertEquals(3, map.size());
        Assert.assertEquals("Jessie", map.get("person"));
        Assert.assertEquals("pet rock", map.get("what"));
        Assert.assertEquals("today", map.get("when"));
    }

    @Test
    public void testExtractDuplicatePlaceholdersMismatch() {

        String template = "{person} bought a {what} in the {what} store {when}";
        String actual = "Jessie bought a doughnut in the music store today";
        Assert.assertFalse(PlaceholderUtil.matches(template, actual));
        Assert.assertNull(PlaceholderUtil.getPlaceholderValues(template, actual));
    }

    @Test
    public void testReplacePlaceholders() {

        Map<String, String> replacements = new HashMap<>();
        replacements.put("person", "Joe");
        replacements.put(" what%#! ", "\"confused? ");
        replacements.put("123", "456");
        replacements.put("foo", "789");

        Assert.assertEquals("Hi Joe!", PlaceholderUtil.replace("Hi {person}!", replacements));
        Assert.assertEquals("Aloha Joe, why so \"confused? ?", PlaceholderUtil.replace("Aloha {person}, why so { what%#! }?", replacements));
        Assert.assertEquals("123456789", PlaceholderUtil.replace("123{123}{foo}", replacements));
    }
}
