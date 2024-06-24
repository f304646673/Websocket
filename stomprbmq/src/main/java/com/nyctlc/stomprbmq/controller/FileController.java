package com.nyctlc.stomprbmq.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class FileController {

    @GetMapping("/")
    public String index() {
        return "index"; // 返回index.html
    }

    @RequestMapping(value = "/favicon.ico")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void favicon() {
        // No operation. Just to avoid 404 error for favicon.ico
    }
}
