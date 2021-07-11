package de.greyshine.webformconsumer.controller;

import de.greyshine.webformconsumer.Service;
import de.greyshine.webformconsumer.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@Slf4j
public class MainController {

    public static final AtomicLong requestCount = new AtomicLong(0);

    @Autowired
    private Service service;

    @GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
    public String test() {
        return "hello-world";
    }

    @PostMapping(value = "/send")
    public ResponseEntity<String> post(HttpServletRequest request, HttpServletResponse response) {

        final String ip = Utils.getClientIpAddr(request);
        final long num = requestCount.addAndGet(1);
        final long bytesWritten = service.handle(num, ip, request.getParameterMap());

        log.info("info handled from {} with {} bytes", ip, bytesWritten);

        return ResponseEntity.ok().body(String.valueOf(num));
    }

}
