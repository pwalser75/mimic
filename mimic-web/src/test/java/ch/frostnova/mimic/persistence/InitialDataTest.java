package ch.frostnova.mimic.persistence;

import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.persistence.entity.MappingEntity;
import ch.frostnova.mimic.persistence.repository.MappingRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test JPA repository
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InitialDataTest {

    @Autowired
    private MappingRepository repository;

    @Test
    public void testInitialDataAvailable() {

        String id = "nzghNv4StIDFqBXaxqDZnWd4kMFfpWfq";
        MappingEntity entity = repository.findOne(id);
        Assert.assertNotNull(entity);
        Assert.assertEquals(RequestMethod.GET, entity.getRequestMethod());
        Assert.assertEquals("/mimic/debug", entity.getPath());
    }
}
