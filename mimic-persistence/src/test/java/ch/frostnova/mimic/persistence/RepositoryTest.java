package ch.frostnova.mimic.persistence;

import ch.frostnova.mimic.api.type.RequestMethod;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Test JPA repository
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTest extends BaseRepositoryTest {

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
    public void testCRUD() throws Exception {


        Supplier<MimicMappingEntity> create = () -> {
            MimicMappingEntity entity = new MimicMappingEntity();
            entity.setRequestMethod(RequestMethod.values()[(int) (RequestMethod.values().length * Math.random())]);
            entity.setPath("/api/{tenant}/resource/{id}");
            entity.setScript("console.log('Just a test')");
            return entity;
        };
        Consumer<MimicMappingEntity> modify = entity -> {
            entity.setRequestMethod(RequestMethod.values()[(int) (RequestMethod.values().length * Math.random())]);
            entity.setPath("/api/{tenant}/resource/{id}");
            entity.setScript("console.log('Just a test')");
        };
        BiConsumer<MimicMappingEntity, MimicMappingEntity> compare = (expected, actual) -> {
            Assert.assertEquals(expected.getRequestMethod(), actual.getRequestMethod());
            Assert.assertEquals(expected.getPath(), actual.getPath());
            Assert.assertEquals(expected.getScript(), actual.getScript());

        };

        testCRUD(repository, create, modify, compare);
    }
}
