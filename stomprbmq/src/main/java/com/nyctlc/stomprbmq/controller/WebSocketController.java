package com.nyctlc.stomprbmq.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/msg-from-user")
    @SendTo("/topic/msg-to-user")
    public String handle(String msg) {
        System.out.println("Received message: " + msg);
        return msg;
    }
}
