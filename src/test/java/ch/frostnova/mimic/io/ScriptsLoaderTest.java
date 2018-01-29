package ch.frostnova.mimic.io;

import ch.frostnova.mimic.api.MimicMapping;
import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.api.type.TemplateExpression;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;

/**
 * Tests for the {@link ScriptsLoader}
 *
 * @author pwalser
 * @since 29.01.2018.
 */
public class ScriptsLoaderTest {

    @Test
    public void testLoadValidMapping() throws IOException {

        for (RequestMethod method : RequestMethod.values()) {

            String path = "/some/{var}/path/{id}";
            String script = "var x=Math.random();\nconsole.log(x);";

            testMapping(method.name(), path, script);
            testMapping(method.name().toLowerCase(), path, script);
            testMapping(method.name().toUpperCase(), path, script);
        }
    }

    private void testMapping(String requestMethod, String path, String script) throws IOException {
        ScriptsLoader loader = new ScriptsLoader();
        StringBuilder builder = new StringBuilder();

        builder.append("// " + requestMethod + " " + path);
        builder.append("\n");
        while (Math.random() > 0.5) {
            builder.append("\n");
        }
        builder.append(script);

        MimicMapping mapping = loader.load(new StringReader(builder.toString()));
        Assert.assertTrue(mapping.getMethod().name().equalsIgnoreCase(requestMethod));
        Assert.assertEquals(path, mapping.getPath());
        Assert.assertEquals(new TemplateExpression(path), mapping.getPathTemplate());
        Assert.assertEquals(script, mapping.getCode().trim());
    }

    @Test(expected = IOException.class)
    public void testLoadMappingUnsupportedRequestMethod() throws IOException {
        String path = "/some/{var}/path/{id}";
        String script = "var x=Math.random();\nconsole.log(x);";

        testMapping("TWEAK", path, script);
    }

    @Test(expected = IOException.class)
    public void testLoadInvalidMappingFormat() throws IOException {
        ScriptsLoader loader = new ScriptsLoader();
        loader.load(new StringReader("214trpqgubvs9 dv"));
    }

    @Test(expected = IOException.class)
    public void testLoadEmptyMapping() throws IOException {
        ScriptsLoader loader = new ScriptsLoader();
        loader.load(new StringReader(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadNullReader() throws IOException {
        ScriptsLoader loader = new ScriptsLoader();
        loader.load((Reader) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadNullBufferedReader() throws IOException {
        ScriptsLoader loader = new ScriptsLoader();
        loader.load((BufferedReader) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadNullPath() throws IOException {
        ScriptsLoader loader = new ScriptsLoader();
        loader.load((Path) null);
    }
}
