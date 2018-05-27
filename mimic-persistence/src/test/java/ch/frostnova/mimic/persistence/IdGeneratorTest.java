package ch.frostnova.mimic.persistence;

import ch.frostnova.mimic.persistence.generator.IdGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author pwalser
 * @since 27.05.2018.
 */
public class IdGeneratorTest {

    @Test
    public void testIdGenerator() {

        IdGenerator idGenerator = new IdGenerator();

        Set<Serializable> generatedIds = new HashSet<>();
        for (int i = 0; i < 1000; i++) {

            Serializable id = idGenerator.generate(null, null);
            Assert.assertNotNull(id);
            Assert.assertFalse(generatedIds.contains(id));
            generatedIds.add(id);
        }
    }
}
