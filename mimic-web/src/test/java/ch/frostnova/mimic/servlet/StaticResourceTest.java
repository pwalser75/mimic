package ch.frostnova.mimic.servlet;

import org.junit.Assert;
import org.junit.Test;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for static resources
 *
 * @author pwalser
 * @since 26.03.2018
 */
public class StaticResourceTest {

    @Test
    public void testMimeTypeGuessing() {

        Map<String, String> tests = new HashMap<>();
        tests.put("test.png", "image/png");
        tests.put("/test.png", "image/png");
        tests.put("some/path/test.png", "image/png");
        tests.put("../test.png", "image/png");

        tests.put("test.jpg", "image/jpeg");
        tests.put("test.txt", "text/plain");
        tests.put("test.html", "text/html");
        tests.put("test.pdf", "application/pdf");
        tests.put("test.bin", "application/octet-stream");

        tests.forEach((k, v) -> {
            String contentType = URLConnection.guessContentTypeFromName(k);
            System.out.println(k + " -> " + contentType);
            if (contentType != null) {
                Assert.assertEquals(v, contentType);
            }
        });
    }

}
