package ch.frostnova.mimic.api;

import ch.frostnova.mimic.api.service.StaticResourceService;
import ch.frostnova.util.check.Check;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Domain object for resource repository, will be injected into the evaluation context of Mimic.
 *
 * @author pwalser
 * @since 29.08.2018.
 */
public class Repository {

    private StaticResourceService resourceService;

    public Repository(StaticResourceService resourceService) {
        Check.required(resourceService, "resourceService");
        this.resourceService = resourceService;
    }

    public StaticResource newResource(String repositoryId, String name, String contentType, String characterData) {
        Check.required(characterData, "characterData");
        return newResource(repositoryId, name, contentType, characterData.getBytes(StandardCharsets.UTF_8));
    }

    public StaticResource newResource(String repositoryId, String name, String contentType, byte[] data) {
        Check.required(repositoryId, "repositoryId");
        Check.required(name, "name");
        Check.required(contentType, "contentType");
        Check.required(data, "data");
        StaticResource resource = new StaticResource();
        resource.setRepositoryId(repositoryId);
        resource.setName(name);
        resource.setContentType(contentType);
        resource.setContent(data);
        return resource;
    }

    public StaticResource example() {
        StaticResource resource = new StaticResource();
        resource.setRepositoryId("example");
        resource.setName("example.jpg");
        resource.setContentType("image/jpeg");

        URL url = getClass().getResource("/example/example.jpg");
        try (InputStream in = url.openStream()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[0xFFFF];
            int read;
            while ((read = in.read(buffer)) >= 0) {
                out.write(buffer, 0, read);
            }
            resource.setContent(out.toByteArray());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return resource;
    }

    public StaticResource save(StaticResource resource) {

        Check.required(resource, "resource");
        return resourceService.save(resource);
    }
}
