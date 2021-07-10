package de.greyshine.webformconsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * https://spring.io/guides/gs/rest-service-cors/
 */
@SpringBootApplication
@Slf4j
public class Main implements ApplicationRunner {

    static final DateTimeFormatter SDF_FILENAME = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");
    static final DateTimeFormatter SDF_READABLE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private String allowedOrigins;
    private File storeDir;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    private static String getArg(ApplicationArguments args, String name, String defaultValue) {

        for (String a : args.getNonOptionArgs()) {
            if (a != null && a.equals(name) || a.startsWith(name + "=")) {
                final int idxEq = a.indexOf('=');
                return idxEq < 0 ? a : a.substring(idxEq + 1).trim();
            }
        }
        log.debug("default {}={}", name, defaultValue);
        return defaultValue;
    }

    private static String getArg2(ApplicationArguments args, String name, String defaultValue) {

        try {
            final String v = args.getOptionValues(name).get(0);
            return v == null || v.trim().isBlank() ? defaultValue : v;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public void run(ApplicationArguments args) {

        // pferd --dir=data --allowedOrigin=* --franz lama fw=auto
        //non-option-args=pferd, lama, fw=auto
        //option-names-args=franz, allowedOrigin, dir

        // log.info("getNonOptionArgs: {}", args.getNonOptionArgs());
        // log.info("getOptionNames: {}", args.getOptionNames());

        // args.getOptionNames().forEach( ao-> System.out.println("allowedOrigin: "+ ao +"="+ args.getOptionValues(ao)) );

        allowedOrigins = getArg(args, "allowedOrigins", "*");
        log.info("allowed-origins: " + allowedOrigins);

        storeDir = new File(getArg(args, "dir", ".")).getAbsoluteFile();
        log.info("store dir: " + storeDir.getAbsolutePath());
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                final String allowedOrigins = Main.this.allowedOrigins == null ? "*" : Main.this.allowedOrigins;
                log.info("allowedOrigins: {}", allowedOrigins);

                registry.addMapping("/send").allowedOrigins(allowedOrigins);
            }
        };
    }

    public long handle(long id, String ip, Map<String, String[]> parameterMap) {

        if (parameterMap == null || parameterMap.isEmpty()) {
            return -1;
        }

        final LocalDateTime ldt = LocalDateTime.now();

        final File file = new File(storeDir, ldt.format(SDF_FILENAME) + "." + id + ".txt");

        final StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append('\n');
        sb.append(SDF_READABLE.format(ldt)).append('\n');
        sb.append("IP: ").append(ip).append('\n');
        sb.append("---------------------------------------").append('\n');
        parameterMap.keySet().stream().sorted().forEach(
                key -> {
                    sb.append("\n").append(key).append(":\n");

                    final String[] vs = parameterMap.get(key);
                    if (vs == null || vs.length == 0) {
                        return;
                    } else if (vs.length == 1) {
                        sb.append(vs[0]);
                    } else {

                        for (int i = 0, l = vs.length; i < l; i++) {
                            sb.append('[').append(i).append("] ").append(vs[i]).append('\n');
                        }
                    }
                }
        );

        return Utils.writeFile(file, sb.toString());
    }
}
