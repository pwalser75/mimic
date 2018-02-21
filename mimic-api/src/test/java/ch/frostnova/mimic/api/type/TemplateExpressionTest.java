package ch.frostnova.mimic.api.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Tests for {@link ch.frostnova.mimic.api.type.TemplateExpression}s (such as "abc{placeholder}def").
 *
 * @author pwalser
 * @since 24.01.2018.
 */
public class TemplateExpressionTest {

    @Test
    public void testToString() {

        Assert.assertEquals("TemplateExpression(template=, keys=[], literal length=0)", new TemplateExpression("").toString());
        Assert.assertEquals("TemplateExpression(template=foo, keys=[], literal length=3)", new TemplateExpression("foo").toString());
        Assert.assertEquals("TemplateExpression(template={foo}, keys=[foo], literal length=0)", new TemplateExpression("{foo}").toString());
        Assert.assertEquals("TemplateExpression(template=bla={foo}, keys=[foo], literal length=4)", new TemplateExpression("bla={foo}").toString());
        Assert.assertEquals("TemplateExpression(template={person} bought a {what} in the {what} store {when}, keys=[person,what,when], literal length=25)", new TemplateExpression("{person} bought a {what} in the {what} store {when}").toString());
    }

    @Test
    public void testOrder() {

        List<TemplateExpression> ordered = Arrays.asList("/a/12345", "/a/b/c", "/a/b/{c}", "/a/{b}/{c}", "/a/b", "/{a}/{b}/{c}")
                .stream().map(TemplateExpression::new).collect(Collectors.toList());

        List<TemplateExpression> list = new ArrayList<>(ordered);
        Collections.shuffle(list);
        Collections.sort(list);

        Assert.assertArrayEquals(ordered.toArray(), list.toArray());
    }

    @Test
    public void testNoPlaceholders() {

        for (String s : Arrays.asList(null, "", " ", "What", "No placeholder {", "} still no { match")) {
            Set<String> placeholders = new TemplateExpression(s).getPlaceholderKeys();
            Assert.assertNotNull(placeholders);
            Assert.assertTrue(placeholders.isEmpty());
        }
    }

    @Test
    public void testPlaceholders() {

        for (String s : Arrays.asList("{foo}", " {foo} ", "abc{foo}def", "Now {foo} twice:{foo}!")) {
            Set<String> placeholders = new TemplateExpression(s).getPlaceholderKeys();
            Assert.assertNotNull(placeholders);
            Assert.assertEquals(1, placeholders.size());
            Assert.assertTrue(placeholders.contains("foo"));
        }

        BiConsumer<String, List<String>> test = (string, expectedKeys) -> {
            Set<String> placeholders = new TemplateExpression(string).getPlaceholderKeys();
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
        Assert.assertTrue(new TemplateExpression(template).matches(actual));

        Map<String, String> map = new TemplateExpression(template).getPlaceholderValues(actual);
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
        Assert.assertFalse(new TemplateExpression(template).matches(actual));
        Assert.assertNull(new TemplateExpression(template).getPlaceholderValues(actual));
    }

    @Test
    public void testExtractDuplicatePlaceholders() {

        String template = "{person} bought a {what} in the {what} store {when}";
        String actual = "Jessie bought a pet rock in the pet rock store today";
        Assert.assertTrue(new TemplateExpression(template).matches(actual));
        Map<String, String> map = new TemplateExpression(template).getPlaceholderValues(actual);

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
        Assert.assertFalse(new TemplateExpression(template).matches(actual));
        Assert.assertNull(new TemplateExpression(template).getPlaceholderValues(actual));
    }

    @Test
    public void testReplacePlaceholders() {

        Map<String, String> replacements = new HashMap<>();
        replacements.put("person", "Joe");
        replacements.put(" what%#! ", "\"confused? ");
        replacements.put("123", "456");
        replacements.put("foo", "789");

        Assert.assertEquals("Hi Joe!", new TemplateExpression("Hi {person}!").process(replacements));
        Assert.assertEquals("Aloha Joe, why so \"confused? ?", new TemplateExpression("Aloha {person}, why so { what%#! }?").process(replacements));
        Assert.assertEquals("123456789", new TemplateExpression("123{123}{foo}").process(replacements));
    }
}
