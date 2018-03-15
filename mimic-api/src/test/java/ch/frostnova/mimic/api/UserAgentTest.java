package ch.frostnova.mimic.api;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link UserAgent}
 *
 * @author pwalser
 * @since 08.02.2018.
 */
public class UserAgentTest {

    @Test
    public void testDetectFirefox() {
        UserAgent userAgent = new UserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0");
        Assert.assertEquals("Windows NT 10.0", userAgent.getOS());
        Assert.assertEquals("Firefox", userAgent.getName());
        Assert.assertEquals("57.0", userAgent.getVersion());
    }

    @Test
    public void testDetectChrome() {
        UserAgent userAgent = new UserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
        Assert.assertEquals("Windows NT 10.0", userAgent.getOS());
        Assert.assertEquals("Chrome", userAgent.getName());
        Assert.assertEquals("63.0.3239.84", userAgent.getVersion());
    }

    @Test
    public void testDetectEdge() {
        UserAgent userAgent = new UserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299");
        Assert.assertEquals("Windows NT 10.0", userAgent.getOS());
        Assert.assertEquals("Edge", userAgent.getName());
        Assert.assertEquals("16.16299", userAgent.getVersion());
    }

    @Test
    public void testDetectSafari() {
        UserAgent userAgent = new UserAgent("Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53");
        Assert.assertEquals("iPad", userAgent.getOS());
        Assert.assertEquals("Safari", userAgent.getName());
        Assert.assertEquals("9537.53", userAgent.getVersion());
    }

    @Test
    public void testDetectOpera() {
        UserAgent userAgent = new UserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.52 Safari/537.36 OPR/15.0.1147.100");
        Assert.assertEquals("Windows NT 6.1", userAgent.getOS());
        Assert.assertEquals("OPR", userAgent.getName());
        Assert.assertEquals("15.0.1147.100", userAgent.getVersion());
    }

    @Test
    public void testDetectGeneric() {
        UserAgent userAgent = new UserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Foo/123");
        Assert.assertEquals("Windows NT 10.0", userAgent.getOS());
        Assert.assertEquals("Mozilla", userAgent.getName());
        Assert.assertEquals("5.0", userAgent.getVersion());
    }

    @Test
    public void testDetectOther() {
        UserAgent userAgent = new UserAgent("Iridium/9.72 (WapOS XCP-220) Foo/123");
        Assert.assertEquals("WapOS XCP-220", userAgent.getOS());
        Assert.assertEquals("Iridium", userAgent.getName());
        Assert.assertEquals("9.72", userAgent.getVersion());
    }

    @Test
    public void testDetectUnknown() {
        UserAgent userAgent = new UserAgent("Bwahaha, no way I'm exposing my real user agent");
        Assert.assertEquals("?", userAgent.getOS());
        Assert.assertEquals("?", userAgent.getName());
        Assert.assertEquals("?", userAgent.getVersion());
    }
}
