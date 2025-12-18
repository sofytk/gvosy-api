package com.sonchasapps.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebSocketPushService {

    private final SimpMessagingTemplate template;

    public WebSocketPushService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendToAssistant(UUID assistantId, Object data) {
        template.convertAndSend("/topic/assistant/" + assistantId, data);
        System.out.println("Sent WebSocket to assistant: " + assistantId);
    }
}

