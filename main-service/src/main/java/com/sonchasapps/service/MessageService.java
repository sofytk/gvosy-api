package com.sonchasapps.service;

import com.sonchasapps.dto.KafkaAiResponse;
import com.sonchasapps.dto.KafkaAudioRequest;
import com.sonchasapps.dto.MessageDTO;
import com.sonchasapps.models.MessageEntity;
import com.sonchasapps.repository.MessageRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final WebSocketPushService pushService;

    public MessageService(MessageRepository repository, KafkaTemplate<String, Object> kafkaTemplate, WebSocketPushService pushService) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.pushService = pushService;
    }

    public MessageDTO createAudioMessage(UUID userId, UUID assistantId, String audioId) {

        MessageEntity entity = new MessageEntity(
                assistantId,
                userId,
                "audio",
                audioId,
                Instant.now()
        );
        entity = repository.save(entity);

        kafkaTemplate.send("audio-to-ai", new KafkaAudioRequest(
                entity.getId(),
                userId,
                assistantId,
                audioId
        ));

        return MessageDTO.fromEntity(entity);
    }

    public void handleAiResponse(KafkaAiResponse ai) {
        MessageEntity msg = repository.findById(ai.getMessageId())
                .orElseThrow();

        msg.setType("text");
        msg.setText(ai.getOriginalText());
        repository.save(msg);

        pushService.sendToAssistant(String.valueOf(ai.getAssistanceId()), MessageDTO.fromEntity(msg));
    }

    public List<MessageDTO> getMessages(UUID assistantId) {
        return repository.findByAssistantId(assistantId)
                .stream().map(MessageDTO::fromEntity)
                .toList();
    }
}

