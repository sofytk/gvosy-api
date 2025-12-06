package com.sonchasapps.service;

import com.sonchasapps.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketPushService {

    private final SimpMessagingTemplate template;

    public WebSocketPushService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendToAssistant(String assistantId, MessageDTO dto) {
        template.convertAndSend("/topic/assistant/" + assistantId, dto);
    }
}

