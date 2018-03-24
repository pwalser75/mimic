package ch.frostnova.mimic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Spring boot application main class
 *
 * @author pwalser
 * @since 23.01.2018.
 */
@SpringBootApplication
@EnableAutoConfiguration
@ServletComponentScan
@ComponentScan
@EnableAspectJAutoProxy
public class MimicBoot {

    public static void main(String[] args) {
        SpringApplication.run(MimicBoot.class, args);
    }
}