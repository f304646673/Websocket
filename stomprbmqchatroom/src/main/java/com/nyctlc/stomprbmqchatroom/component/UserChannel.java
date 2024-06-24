package com.nyctlc.stomprbmqchatroom.component;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class UserChannel {
    private final ConcurrentHashMap<String, MessageChannel> userMap = new ConcurrentHashMap<>();

    public MessageChannel getChannel(String userId) {
        return userMap.get(userId);
    }

    public void addChannel(String userId, MessageChannel channel) {
        userMap.put(userId, channel);
    }

    public void removeChannel(String userId) {
        userMap.remove(userId);
    }

    public ConcurrentHashMap<String, MessageChannel> getUsers() {
        return userMap;
    }
}