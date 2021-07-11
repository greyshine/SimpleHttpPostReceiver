package de.greyshine.webformconsumer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@org.springframework.stereotype.Service
@Slf4j
public class Service {

    public static final DateTimeFormatter SDF_FILENAME = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");
    public static final DateTimeFormatter SDF_READABLE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Value("${dir:.}")
    public File storeDir;

    @PostConstruct
    @SneakyThrows
    public void postConstruct() {
        storeDir = storeDir.getCanonicalFile();
        log.info("store dir: " + storeDir);
    }

    public long handle(long id, String ip, Map<String, String[]> parameterMap) {

        if (parameterMap == null || parameterMap.isEmpty()) {
            return -1;
        }

        final LocalDateTime ldt = LocalDateTime.now();

        final File file = new File(storeDir, ldt.format(SDF_FILENAME) + "." + id + ".txt");

        final AtomicBoolean foundValues = new AtomicBoolean(false);

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

                        foundValues.set(foundValues.get() || (vs[0] != null && !vs[0].trim().isEmpty()));

                    } else {

                        for (int i = 0, l = vs.length; i < l; i++) {
                            sb.append('[').append(i).append("] ").append(vs[i]).append('\n');
                            foundValues.set(foundValues.get() || (vs[i] != null && !vs[i].trim().isEmpty()));
                        }
                    }
                }
        );

        if (!foundValues.get()) {
            return -1;
        }

        return Utils.writeFile(file, sb.toString());
    }
}
