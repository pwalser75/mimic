package ch.frostnova.mimic.service;

import ch.frostnova.mimic.api.StaticResource;
import ch.frostnova.mimic.api.service.StaticResourceService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Tests for {@link StaticResourceService}
 *
 * @author pwalser
 * @since 28.08.2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StaticResourceServiceTest {

    @Autowired
    private StaticResourceService staticResourceService;

    @Test
    public void test() {

        StaticResource resource = new StaticResource();
        resource.setRepositoryId("TEST#" + UUID.randomUUID());
        resource.setName("foo/test.png");
        resource.setContentType("image/png");
        resource.setContent(randomData());
        Assert.assertEquals(resource.getContent().length, resource.getSize());

        StaticResource saved = staticResourceService.save(resource);
        Assert.assertNotNull(saved);
        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(resource.getRepositoryId(), saved.getRepositoryId());
        Assert.assertEquals(resource.getName(), saved.getName());
        Assert.assertEquals(resource.getContentType(), saved.getContentType());
        Assert.assertArrayEquals(resource.getContent(), saved.getContent());
        Assert.assertEquals(resource.getSize(), saved.getSize());

        StaticResource restored = staticResourceService.load(saved.getId());
        Assert.assertNotNull(restored);
        Assert.assertNotNull(restored.getId());
        Assert.assertEquals(saved.getId(), restored.getId());
        Assert.assertEquals(saved.getRepositoryId(), restored.getRepositoryId());
        Assert.assertEquals(saved.getName(), restored.getName());
        Assert.assertEquals(saved.getContentType(), restored.getContentType());
        Assert.assertArrayEquals(saved.getContent(), restored.getContent());
        Assert.assertEquals(saved.getSize(), restored.getSize());

        saved.setRepositoryId("OTHER#" + UUID.randomUUID());
        saved.setName("changed/foo.jpg");
        saved.setContentType("image/jpeg");
        saved.setContent(randomData());
        staticResourceService.save(saved);

        restored = staticResourceService.get(saved.getRepositoryId(), saved.getName());
        Assert.assertNotNull(restored);
        Assert.assertNotNull(restored.getId());
        Assert.assertEquals(saved.getId(), restored.getId());
        Assert.assertEquals(saved.getRepositoryId(), restored.getRepositoryId());
        Assert.assertEquals(saved.getName(), restored.getName());
        Assert.assertEquals(saved.getContentType(), restored.getContentType());
        Assert.assertArrayEquals(saved.getContent(), restored.getContent());
        Assert.assertEquals(saved.getSize(), restored.getSize());

        StaticResource notFound = staticResourceService.get("foo", "bar");
        Assert.assertNull(notFound);
        notFound = staticResourceService.load("@12345");
        Assert.assertNull(notFound);

        staticResourceService.delete(saved.getId());
        StaticResource deleted = staticResourceService.load(saved.getId());
        Assert.assertNull(deleted);

        staticResourceService.delete("no such resource");
    }

    private byte[] randomData() {
        byte[] data = new byte[1 + (int) (Math.random() * 0xFFFF)];
        ThreadLocalRandom.current().nextBytes(data);
        return data;
    }

}
