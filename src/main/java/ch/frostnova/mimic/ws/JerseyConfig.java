package org.test.spring.boot.project.ws;

import ch.frostnova.mimic.ws.MappingsEndpoint;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.test.spring.boot.project.ws.provider.CORSFilter;
import org.test.spring.boot.project.ws.provider.NoSuchElementExceptionMapper;

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
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage("org.test.spring.boot.project.ws");
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
    }
}
