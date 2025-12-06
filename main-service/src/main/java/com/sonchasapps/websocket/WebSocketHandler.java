package com.sonchasapps.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<UUID, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void afterConnectionEstablished(WebSocketSession session) {
        UUID userId = UUID.fromString(session.getUri().getQuery().split("=")[1]);
        sessions.put(userId, session);
    }


    public void sendToUser(UUID userId, Object msg) {
        try {
            WebSocketSession session = sessions.get(userId);
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(msg)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
