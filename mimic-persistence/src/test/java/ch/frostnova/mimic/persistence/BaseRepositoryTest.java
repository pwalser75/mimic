package ch.frostnova.mimic.persistence;

import org.junit.Assert;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Base repository test
 *
 * @author pwalser
 * @since 24.03.2018.
 */
public abstract class BaseRepositoryTest {

    public <T extends BaseEntity> void testCRUD(CrudRepository<T, String> repository,
                                                Supplier<T> create, Consumer<T> modify, BiConsumer<T, T> compare) throws Exception {
        Assert.assertNotNull(repository);
        Assert.assertNotNull(create);
        Assert.assertNotNull(modify);
        Assert.assertNotNull(compare);

        final T entity = create.get();
        Assert.assertNotNull(entity);
        Assert.assertFalse(entity.isPersistent());

        // create

        final T saved = repository.save(entity);
        Assert.assertTrue(saved.isPersistent());
        Assert.assertNotNull(saved.getId());
        Assert.assertNotNull(saved.getCreatedAt());
        Assert.assertNotNull(saved.getLastModifiedAt());
        Assert.assertEquals(saved.getCreatedAt(), saved.getLastModifiedAt());
        Assert.assertEquals(0, saved.getVersion());
        compare.accept(entity, saved);
        final LocalDateTime createdAt = saved.getCreatedAt();

        final T other = repository.save(create.get());
        Assert.assertEquals(entity, saved);
        Assert.assertNotEquals(entity, other);

        // read

        final T read = repository.findOne(saved.getId());
        Assert.assertEquals(saved.getId(), read.getId());
        Assert.assertNotNull(read.getCreatedAt());
        Assert.assertNotNull(read.getLastModifiedAt());
        Assert.assertEquals(createdAt, read.getCreatedAt());
        Assert.assertEquals(read.getCreatedAt(), read.getLastModifiedAt());
        Assert.assertEquals(0, entity.getVersion());
        Assert.assertEquals(entity, read);

        // update
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {

        }
        modify.accept(read);
        repository.save(read);
        final T updated = repository.findOne(read.getId());
        Assert.assertEquals(read.getId(), updated.getId());
        Assert.assertEquals(createdAt, updated.getCreatedAt());
        Assert.assertTrue(updated.getLastModifiedAt().isAfter(read.getCreatedAt()));
        Assert.assertEquals(1, updated.getVersion());
        Assert.assertEquals(entity, updated);
        compare.accept(read, updated);

        // delete
        repository.delete(saved.getId());
        final T deleted = repository.findOne(saved.getId());
        Assert.assertNull(deleted);
    }

}
