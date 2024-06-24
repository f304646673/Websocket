package com.nyctlc.stomprbmqchatroom.service;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyctlc.stomprbmqchatroom.component.UserChannel;

@Service
public class ChatService {
    
    @Autowired
    private UserChannel userChannel;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send2Others(String msg, String uid) {
        for (String key : userChannel.getUsers().keySet()) {
            if (!key.equals(uid)) {
                JSONObject json = new JSONObject(msg);
                json.put("uid", uid);
                rabbitTemplate.convertAndSend("amq.topic", key, json.toString());
            }
        }
    }
    
}
