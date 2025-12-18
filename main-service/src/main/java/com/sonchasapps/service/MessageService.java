package com.sonchasapps.service;

import com.sonchasapps.dto.AssistantResponse;
import com.sonchasapps.dto.KafkaAiResponse;
import com.sonchasapps.dto.KafkaAudioRequest;
import com.sonchasapps.dto.MessageDTO;
import com.sonchasapps.models.AssistantEntity;
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
    private final AssistantService assistantService;
    private final NoteService noteService;

    public MessageService(
            MessageRepository repository,
            AssistantService assistantService,
            KafkaTemplate<String, Object> kafkaTemplate,
            WebSocketPushService pushService,
            NoteService noteService
    ) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.pushService = pushService;
        this.assistantService = assistantService;
        this.noteService = noteService;
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

        AssistantResponse assistant = assistantService.getAssistantByUserId(userId);

        kafkaTemplate.send("audio.transcription.request", new KafkaAudioRequest(
                entity.getId(),
                assistant.getDescription(),
                userId,
                assistant.getAge(),
                assistant.getSex(),
                audioId
        ));

        return MessageDTO.fromEntity(entity);
    }

    public void handleAiResponse(KafkaAiResponse ai) {
        MessageEntity msg = repository.findById(ai.getMessageId())
                .orElseThrow(() -> new RuntimeException("Message not found: " + ai.getMessageId()));

        msg.setType("text");
        msg.setText(ai.getOriginalText());
        repository.save(msg);

        System.out.println("Updated message in MongoDB: " + ai.getMessageId());

        UUID assistantId = assistantService.getAssistantIdByUserId(ai.getUserId());
        pushService.sendToAssistant(assistantId, MessageDTO.fromEntity(msg));

        noteService.handleNoteCreation(ai);

        System.out.println("Handled AI response for message: " + ai.getMessageId());
    }

    public List<MessageDTO> getMessages(UUID assistantId) {
        return repository.findByAssistantId(assistantId)
                .stream()
                .map(MessageDTO::fromEntity)
                .toList();
    }
}
