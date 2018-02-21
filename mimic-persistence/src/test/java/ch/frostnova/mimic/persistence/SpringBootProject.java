package ch.frostnova.mimic.persistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring boot application main class
 *
 * @author pwalser
 * @since 23.01.2018.
 */
@SpringBootApplication
@ServletComponentScan
@ComponentScan
public class SpringBootProject {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootProject.class, args);
    }
}
