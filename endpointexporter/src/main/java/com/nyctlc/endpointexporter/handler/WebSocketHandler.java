package com.nyctlc.endpointexporter.handler;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocketHandler {

    private static final ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    // WebSocket handler implementation
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId){
        sessionPools.put(userId, session);
        // WebSocket onOpen implementation
    }

    @OnClose
    public void onClose(Session session, @PathParam(value = "userId") String userId){
        // WebSocket onClose implementation
    }

    // Other WebSocket methods
    @OnMessage
    public void onMessage(Session session, String message, @PathParam(value = "userId") String userId){
        // WebSocket onMessage implementation
        for (Session s : sessionPools.values()) {
            if (s.equals(session)) {
                continue;
            }
            s.getAsyncRemote().sendText(userId+" said: " + message);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable, @PathParam(value = "userId") String userId){
        // WebSocket onError implementation
    }
}