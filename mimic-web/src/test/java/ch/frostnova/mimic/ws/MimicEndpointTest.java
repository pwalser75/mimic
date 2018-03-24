package ch.frostnova.mimic.ws;

import ch.frostnova.mimic.api.MimicMapping;
import ch.frostnova.mimic.api.type.RequestMethod;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.NotFoundException;

/**
 * Mimic mapping endpoint test
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MimicEndpointTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @LocalServerPort
    private int port;

    @Test
    public void testCRUD() {

        final String baseURL = "https://localhost:" + port + "/mimic/mappings";
        log.info("BASE URL: " + baseURL);
        try (final MimicClient mimicClient = new MimicClient(baseURL)) {

            // create

            MimicMapping mapping = new MimicMapping();
            mapping.setDisplayName("Test-Mapping");
            mapping.setDescription("Integration Test");
            mapping.setMethod(RequestMethod.values()[(int) (RequestMethod.values().length * Math.random())]);
            mapping.setPath("/api/{tenant}/resource/{id}");
            mapping.setScript("console.log('Just a test')");

            MimicMapping created = mimicClient.create(mapping);
            Assert.assertNotNull(created);
            Assert.assertNotNull(created.getId());
            Assert.assertEquals(mapping.getMethod(), created.getMethod());
            Assert.assertEquals(mapping.getPath(), created.getPath());
            Assert.assertEquals(mapping.getScript(), created.getScript());
            String id = created.getId();
            mapping = created;

            // read

            MimicMapping loaded = mimicClient.get(id);
            Assert.assertNotNull(loaded);
            Assert.assertNotNull(loaded.getId());
            Assert.assertEquals(mapping.getMethod(), loaded.getMethod());
            Assert.assertEquals(mapping.getPath(), loaded.getPath());
            Assert.assertEquals(mapping.getScript(), loaded.getScript());

            // list

            Assert.assertTrue(mimicClient.list().stream().anyMatch(p -> p.getId().equals(id)));

            // update

            mapping.setMethod(RequestMethod.values()[(int) (RequestMethod.values().length * Math.random())]);
            mapping.setPath("/another/{path}");
            mapping.setScript("console.log('Way to go')");
            mimicClient.save(mapping);

            loaded = mimicClient.get(id);
            Assert.assertNotNull(loaded);
            Assert.assertEquals(mapping.getId(), loaded.getId());
            Assert.assertEquals(mapping.getMethod(), loaded.getMethod());
            Assert.assertEquals(mapping.getPath(), loaded.getPath());
            Assert.assertEquals(mapping.getScript(), loaded.getScript());

            // delete

            mimicClient.delete(id);

            // delete again - must not result in an exception
            mimicClient.delete(id);

            // must not be found afterwards
            Assert.assertFalse(mimicClient.list().stream().anyMatch(p -> p.getId() == id));

            try {
                mimicClient.get(id);
                Assert.fail("Expected: NotFoundException");
            } catch (NotFoundException expected) {
                //
            }
        }
    }
}
