package ch.frostnova.mimic.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Logger providers, allows the logger to be injected anywhere.
 *
 * @author pwalser
 * @since 25.01.2018.
 */
@Component
public class LoggerProvider {

    @Bean
    @Scope("prototype")
    Logger logger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getField().getDeclaringClass());
    }
}
