package com.sonchasapps.kafka;

import com.sonchasapps.dto.KafkaAiResponse;
import com.sonchasapps.models.MessageEntity;
import com.sonchasapps.repository.MessageRepository;
import com.sonchasapps.service.NoteService;
import com.sonchasapps.websocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AiResponseConsumer {

    private final MessageRepository messageRepository;
    private final NoteService noteService;
    private final WebSocketHandler ws;

    public AiResponseConsumer(MessageRepository messageRepository, NoteService noteService, WebSocketHandler ws) {
        this.messageRepository = messageRepository;
        this.noteService = noteService;
        this.ws = ws;
    }

    @KafkaListener(topics = "audio.transcription.response", groupId = "main-service-group")
    public void consumeAiResponse(KafkaAiResponse response) {

        MessageEntity message = new MessageEntity();
        message.setType("assistant_text");
        message.setUserId(response.getUserId());
        message.setText(response.getSummary());
        message.setCreatedAt(Instant.now());

        messageRepository.save(message);
        noteService.handleNoteCreation(response);
        ws.sendToUser(response.getUserId(), message);
    }
}
