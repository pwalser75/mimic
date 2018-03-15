package ch.frostnova.mimic.api;

import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.api.util.JsonUtil;
import ch.frostnova.mimic.api.util.RandomUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Function;

/**
 * Tests for {@link MimicMapping}
 *
 * @author pwalser
 * @since 15.03.2018.
 */
public class MimicMappingTest {

    @Test
    public void testJsonSerialization() {

        MimicMapping mapping = new MimicMapping();
        testJsonSerialization(mapping);

        mapping.setMethod(RandomUtil.rnd(RequestMethod.class));
        mapping.setPath(RandomUtil.rnd("/", "/foo", "bla/{argh}/{:id}"));
        mapping.setScript(RandomUtil.rnd("foo", "   \narg\n "));
        mapping.setId(UUID.randomUUID().toString());
        mapping.setCreatedAt(RandomUtil.rndDateTime(100, ChronoUnit.YEARS));
        mapping.setLastModifiedAt(RandomUtil.rndDateTime(100, ChronoUnit.YEARS));
        testJsonSerialization(mapping);
    }

    private void testJsonSerialization(MimicMapping mapping) {

        String json = JsonUtil.stringify(mapping);
        System.out.println(json);

        MimicMapping restored = JsonUtil.parse(MimicMapping.class, json);

        testEquals(mapping, restored,
                MimicMapping::getId,
                MimicMapping::getCreatedAt,
                MimicMapping::getLastModifiedAt,
                MimicMapping::getMethod,
                MimicMapping::getPath,
                MimicMapping::getScript);
    }

    private <T> void testEquals(T expected, T value, Function<T, ?>... properties) {
        for (Function<T, ?> property : properties) {
            Assert.assertEquals(property.apply(expected), property.apply(value));
        }
    }
}
