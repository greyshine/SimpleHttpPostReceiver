package de.greyshine.webformconsumer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * https://spring.io/guides/gs/rest-service-cors/
 */
@SpringBootApplication
@Slf4j
public class Main implements ApplicationRunner, ApplicationListener<ContextRefreshedEvent> {

    @Value("${allowedOrigins:*}")
    public String paramAllowOrigins;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    @SneakyThrows
    public void run(ApplicationArguments args) {

        // pferd --dir=data --allowedOrigin=* --franz lama fw=auto
        //non-option-args=pferd, lama, fw=auto
        //option-names-args=franz, allowedOrigin, dir

        // log.info("getNonOptionArgs: {}", args.getNonOptionArgs());
        // log.info("getOptionNames: {}", args.getOptionNames());
        // args.getOptionNames().forEach( ao-> System.out.println("allowedOrigin: "+ ao +"="+ args.getOptionValues(ao)) );
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                log.info("ALLOWED-ORIGINS: {}", paramAllowOrigins);
                registry.addMapping("/send").allowedOrigins(paramAllowOrigins);
            }
        };
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //log.info("onApplicationEvent: {}", contextRefreshedEvent);
    }
}
