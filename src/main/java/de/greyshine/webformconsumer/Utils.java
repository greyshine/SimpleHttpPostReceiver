package de.greyshine.webformconsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public abstract class Utils {

    private Utils() {
    }

    /**
     * https://stackoverflow.com/a/21529994/845117
     *
     * @param request
     * @return
     */
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static long writeFile(File file, String s) {

        s = s == null ? "" : s;

        file.getParentFile().mkdirs();

        try (FileWriter f = new FileWriter(file)) {

            f.write(s);
            f.flush();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return file.length();
    }

    public static String getArg(ApplicationArguments args, String name, String defaultValue) {

        for (String a : args.getNonOptionArgs()) {
            if (a != null && a.equals(name) || a.startsWith(name + "=")) {
                final int idxEq = a.indexOf('=');
                return idxEq < 0 ? a : a.substring(idxEq + 1).trim();
            }
        }
        log.debug("default {}={}", name, defaultValue);
        return defaultValue;
    }
}
