package com.sonchasapps.kafka;

import com.sonchasapps.dto.KafkaAiResponse;
import com.sonchasapps.service.MessageService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AiKafkaListener {

    private final MessageService messageService;

    public AiKafkaListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = "ai-to-main", containerFactory = "aiKafkaListener")
    public void listen(KafkaAiResponse response) {
        messageService.handleAiResponse(response);
    }
}
