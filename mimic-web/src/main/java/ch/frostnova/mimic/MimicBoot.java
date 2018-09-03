package ch.frostnova.mimic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring boot application main class
 *
 * @author pwalser
 * @since 23.01.2018.
 */
@SpringBootApplication
@ServletComponentScan
@EnableAspectJAutoProxy
@EnableScheduling
public class MimicBoot {

    public static void main(String[] args) {
        SpringApplication.run(MimicBoot.class, args);
    }
}