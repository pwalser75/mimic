package ch.frostnova.mimic.ws;

import ch.frostnova.mimic.api.MimicMapping;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;

/**
 * Mimic client API
 */
public class MimicClient implements AutoCloseable {

    private final String baseURL;
    private final Client client;

    public MimicClient(String baseURL) {
        this.baseURL = baseURL;
        ClientBuilder clientBuilder = createClientBuilder();
        client = clientBuilder.build();
    }

    @Override
    public void close() {
        client.close();
    }

    private static ClientBuilder createClientBuilder() {

        try (InputStream in = MimicClient.class.getResourceAsStream("/client-truststore.jks")) {
            KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            truststore.load(in, "truststore".toCharArray());

            return ClientBuilder.newBuilder()
                    .trustStore(truststore)
                    .property(ClientProperties.CONNECT_TIMEOUT, 500)
                    .property(ClientProperties.READ_TIMEOUT, 5000)
                    .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
                    .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "WARNING")
                    .hostnameVerifier((hostname, sslSession) -> "localhost".equals(hostname));
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load client truststore", ex);
        }
    }

    /**
     * List all mappings
     *
     * @return list of mappings (never null)
     */
    public List<MimicMapping> list() {
        Invocation invocation = client
                .target(baseURL)
                .request()
                .buildGet();

        Response response = ResponseExceptionMapper.check(invocation.invoke(), 200);
        return response.readEntity(new GenericType<List<MimicMapping>>() {
        });
    }

    /**
     * Get a mapping by id. Throws a {@link javax.ws.rs.NotFoundException} if the mapping wasn't found.
     *
     * @param id id
     * @return mapping.
     */
    public MimicMapping get(String id) {
        Invocation invocation = client
                .target(baseURL + "/" + id)
                .request()
                .buildGet();

        Response response = ResponseExceptionMapper.check(invocation.invoke(), 200);
        return response.readEntity(MimicMapping.class);
    }

    /**
     * Create a new mapping with the provided data
     *
     * @param mapping data
     * @return created mapping
     */
    public MimicMapping create(MimicMapping mapping) {
        Invocation invocation = client
                .target(baseURL)
                .request()
                .buildPost(Entity.json(mapping));

        Response response = ResponseExceptionMapper.check(invocation.invoke(), 200);
        return response.readEntity(MimicMapping.class);
    }

    /**
     * Update a mapping
     *
     * @param mapping mapping (whose id is required)
     */
    public void save(MimicMapping mapping) {

        if (mapping.getId() == null) {
            throw new IllegalArgumentException("Not yet persisted, use the create() method instead");
        }

        Invocation invocation = client
                .target(baseURL + "/" + mapping.getId())
                .request()
                .buildPut(Entity.json(mapping));

        ResponseExceptionMapper.check(invocation.invoke(), 204);
    }

    /**
     * Delete the mapping with the given id, if it exists (no error thrown otherwise).
     *
     * @param id id of the record
     */
    public void delete(String id) {

        Invocation invocation = client
                .target(baseURL + "/" + id)
                .request()
                .buildDelete();

        ResponseExceptionMapper.check(invocation.invoke(), 204);
    }

}
