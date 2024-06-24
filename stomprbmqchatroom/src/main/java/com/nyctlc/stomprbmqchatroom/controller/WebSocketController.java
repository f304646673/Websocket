package com.nyctlc.stomprbmqchatroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.nyctlc.stomprbmqchatroom.service.ChatService;

@Controller
public class WebSocketController {

    @Autowired
    private ChatService chatService;
    
    @MessageMapping("/msg-from-user")
    public String handle(@Payload String msg, SimpMessageHeaderAccessor headerAccessor) {
        // 从会话属性中获取uid
        String uid = (String) headerAccessor.getSessionAttributes().get("uid");
        System.out.println("Received message: " + msg + " from user ID: " + uid);
        chatService.send2Others(msg, uid);
        return msg;
    }
}
