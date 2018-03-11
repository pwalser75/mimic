package ch.frostnova.mimic.persistence;

import ch.frostnova.mimic.api.type.RequestMethod;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test JPA repository
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestJpaConfig.class)
@SpringBootTest
public class RepositoryTest {

    @Autowired
    private MimicMappingRepository repository;

    @Test
    public void testInitialDataAvailable() {

        String id = "nzghNv4StIDFqBXaxqDZnWd4kMFfpWfq";
        MimicMappingEntity entity = repository.findOne(id);
        Assert.assertNotNull(entity);
        Assert.assertEquals(RequestMethod.GET, entity.getRequestMethod());
        Assert.assertEquals("/mimic/debug", entity.getPath());
    }

    @Test
    public void testCRUD() {

        // create
        MimicMappingEntity entity = new MimicMappingEntity();
        entity.setRequestMethod(RequestMethod.values()[(int) (RequestMethod.values().length * Math.random())]);
        entity.setPath("/api/{tenant}/resource/{id}");
        entity.setScript("console.log('Just a test')");

        Assert.assertFalse(entity.isPersistent());
        entity = repository.save(entity);
        Assert.assertTrue(entity.isPersistent());
        Assert.assertNotNull(entity.getId());

        // read
        MimicMappingEntity read = repository.findOne(entity.getId());
        Assert.assertEquals(entity.getId(), read.getId());
        Assert.assertEquals(entity.getRequestMethod(), read.getRequestMethod());
        Assert.assertEquals(entity.getPath(), read.getPath());
        Assert.assertEquals(entity.getScript(), read.getScript());

        // update
        entity.setPath("/something/else");
        entity.setRequestMethod(RequestMethod.values()[(int) (RequestMethod.values().length * Math.random())]);
        entity.setScript("console.log('lol')");
        entity = repository.save(entity);
        read = repository.findOne(entity.getId());
        Assert.assertEquals(entity.getId(), read.getId());
        Assert.assertEquals(entity.getRequestMethod(), read.getRequestMethod());
        Assert.assertEquals(entity.getPath(), read.getPath());
        Assert.assertEquals(entity.getScript(), read.getScript());

        // delete
        repository.delete(entity.getId());
        entity = repository.findOne(entity.getId());
        Assert.assertNull(entity);
    }
}
