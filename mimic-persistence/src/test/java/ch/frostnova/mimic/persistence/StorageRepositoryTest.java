package ch.frostnova.mimic.persistence;

import ch.frostnova.mimic.persistence.entity.StorageEntity;
import ch.frostnova.mimic.persistence.repository.StorageRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Storage repository JPA test
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StorageRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private StorageRepository repository;

    @Test
    public void testCRUD() throws Exception {

        Supplier<StorageEntity> create = this::randomEntity;

        Consumer<StorageEntity> modify = entity -> {

            byte[] data = new byte[1 + (int) (Math.random() * 0xFFFF)];
            ThreadLocalRandom.current().nextBytes(data);

            entity.setRepositoryId(UUID.randomUUID().toString());
            entity.setResourceId("foo-bar.png");
            entity.setContentType("image/png");
            entity.setContentLength(data.length);
            entity.setContent(data);
        };
        BiConsumer<StorageEntity, StorageEntity> compare = (expected, actual) -> {
            Assert.assertEquals(expected.getRepositoryId(), actual.getRepositoryId());
            Assert.assertEquals(expected.getResourceId(), actual.getResourceId());
            Assert.assertEquals(expected.getContentType(), actual.getContentType());
            Assert.assertEquals(expected.getContentLength(), actual.getContentLength());
            Assert.assertArrayEquals(expected.getContent(), actual.getContent());

        };

        testCRUD(repository, create, modify, compare);
    }

    private StorageEntity randomEntity() {
        byte[] data = new byte[1 + (int) (Math.random() * 0xFFFF)];
        ThreadLocalRandom.current().nextBytes(data);

        StorageEntity entity = new StorageEntity();
        entity.setRepositoryId(UUID.randomUUID().toString());
        entity.setResourceId("test-" + UUID.randomUUID().toString() + ".jpg");
        entity.setContentType("image/jpeg");
        entity.setContentLength(data.length);
        entity.setContent(data);
        return entity;
    }

    @Test
    public void testFindByRepositoryAndResourceId() {

        StorageEntity e1 = randomEntity();
        StorageEntity e2 = randomEntity();
        StorageEntity e3 = randomEntity();

        e2.setRepositoryId(e1.getRepositoryId());
        e3.setResourceId(e1.getResourceId());

        List<StorageEntity> entities = Arrays.asList(e1, e2, e3);
        repository.save(entities);

        for (StorageEntity entity : entities) {
            StorageEntity found = repository.findByRepositoryIdAndResourceId(entity.getRepositoryId(), entity.getResourceId());
            Assert.assertNotNull(found);
            Assert.assertEquals(entity, found);
            Assert.assertEquals(entity.getRepositoryId(), found.getRepositoryId());
            Assert.assertEquals(entity.getResourceId(), found.getResourceId());
        }
    }
}
