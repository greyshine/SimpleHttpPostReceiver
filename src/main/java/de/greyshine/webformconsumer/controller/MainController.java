package de.greyshine.webformconsumer.controller;

import de.greyshine.webformconsumer.Main;
import de.greyshine.webformconsumer.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@Slf4j
public class MainController {

    public static final AtomicLong requestCount = new AtomicLong(0);

    @Autowired
    private Main main;

    @GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
    public ModelAndView test() {

        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test.html");
        return modelAndView;
    }

    @PostMapping(value = "/send")
    public ResponseEntity<String> post(HttpServletRequest request, HttpServletResponse response) {

        final String ip = Utils.getClientIpAddr(request);
        final long num = requestCount.addAndGet(1);
        final long bytesWritten = main.handle(num, ip, request.getParameterMap());

        log.info("info handled from {} with {} bytes", ip, bytesWritten);

        return ResponseEntity.ok().body(String.valueOf(num));
    }

}
