package com.sonchasapps.service;

import com.sonchasapps.dto.AssistantResponse;
import com.sonchasapps.dto.KafkaAiResponse;
import com.sonchasapps.dto.KafkaAudioRequest;
import com.sonchasapps.dto.MessageDTO;
import com.sonchasapps.models.messages.MessageEntity;
import com.sonchasapps.models.messages.MessageRole;
import com.sonchasapps.models.messages.MessageStatus;
import com.sonchasapps.repository.mongo.MessageRepository;
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

    public MessageDTO createAudioMessage(UUID userId,  String audioUrl, String conversationId) {
        if (conversationId == null || conversationId.isEmpty()) {
            conversationId = UUID.randomUUID().toString();
        }
        UUID assistantId = assistantService.getAssistantIdByUserId(userId);
        MessageEntity entity = new MessageEntity();
        entity.setUserId(userId);
        entity.setConversationId(conversationId);
        entity.setRole(MessageRole.USER);
        entity.setContent("");
        entity.setAudioUrl(audioUrl);
        entity.setStatus(MessageStatus.PENDING);
        entity.setAssistantId(assistantId);
        entity.setCreatedAt(Instant.now());

        entity = repository.save(entity);
        System.out.println("Saved USER message: " + entity.getId());

        AssistantResponse assistant = assistantService.getAssistantByUserId(userId);
        kafkaTemplate.send("audio.transcription.request", new KafkaAudioRequest(
                entity.getId(),
                assistant.getDescription(),
                userId,
                assistant.getAge(),
                assistant.getSex(),
                audioUrl
        ));

        pushService.sendToAssistant(assistantId, MessageDTO.fromEntity(entity));

        return MessageDTO.fromEntity(entity);
    }


    public void handleAiResponse(KafkaAiResponse ai) {
        MessageEntity userMessage = repository.findById(ai.getMessageId())
                .orElseThrow(() -> new RuntimeException("Message not found: " + ai.getMessageId()));

        userMessage.setContent(ai.getOriginalText());
        userMessage.setStatus(MessageStatus.COMPLETED);
        userMessage.setUpdatedAt(Instant.now());
        repository.save(userMessage);


        UUID assistantId = userMessage.getAssistantId();
        pushService.sendToAssistant(assistantId, MessageDTO.fromEntity(userMessage));

        noteService.handleNoteCreation(ai);

        createAssistantMessage(
                userMessage.getUserId(),
                userMessage.getConversationId(),
                ai.getSummary(),
                userMessage.getAssistantId()
        );
    }

    public MessageEntity createAssistantMessage(
            UUID userId,
            String conversationId,
            String content,
            UUID assistantId
    ) {
        MessageEntity assistantMessage = new MessageEntity();
        assistantMessage.setUserId(userId);
        assistantMessage.setConversationId(conversationId);
        assistantMessage.setRole(MessageRole.ASSISTANT);
        assistantMessage.setContent(content);
        assistantMessage.setStatus(MessageStatus.COMPLETED);
        assistantMessage.setAssistantId(assistantId);
        assistantMessage.setCreatedAt(Instant.now());

        assistantMessage = repository.save(assistantMessage);

        System.out.println("Created ASSISTANT message: " + assistantMessage.getId());

        pushService.sendToAssistant(assistantId, MessageDTO.fromEntity(assistantMessage));

        return assistantMessage;
    }

    public List<MessageDTO> getConversation(String conversationId) {
        return repository.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(MessageDTO::fromEntity)
                .toList();
    }


    public List<MessageDTO> getUserMessages(UUID userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(MessageDTO::fromEntity)
                .toList();
    }
}
