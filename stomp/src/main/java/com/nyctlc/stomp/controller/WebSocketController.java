package com.nyctlc.stomp.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import jakarta.websocket.server.PathParam;

@Controller
public class WebSocketController {
    @MessageMapping("/msg-from-user")
    @SendTo("/receive/msg-to-user")
    public String handle(String msg) {
        System.out.println("Received message: " + msg);
        return msg;
    }
}
