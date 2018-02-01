package ch.frostnova.mimic.ws;

import ch.frostnova.mimic.ws.provider.CORSFilter;
import ch.frostnova.mimic.ws.provider.NoSuchElementExceptionMapper;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

/**
 * JAX-RS configuration
 */
@Configuration
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        registerEndpoints();
        configureSwagger();
    }

    private void registerEndpoints() {
        register(MappingsEndpoint.class);
        register(CORSFilter.class);
        register(NoSuchElementExceptionMapper.class);
    }

    private void configureSwagger() {
        register(ApiListingResource.class);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"https"});
        beanConfig.setHost("localhost");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage(getClass().getPackage().getName());
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
    }
}
