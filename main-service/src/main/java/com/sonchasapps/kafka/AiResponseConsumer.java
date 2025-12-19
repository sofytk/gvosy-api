package com.sonchasapps.kafka;

import com.sonchasapps.dto.KafkaAiResponse;
import com.sonchasapps.service.MessageService;
import com.sonchasapps.service.NoteService;
import com.sonchasapps.websocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiResponseConsumer {

    private final MessageService messageService;
    private final NoteService noteService;
    private final WebSocketHandler ws;

    @KafkaListener(
            topics = "audio.transcription.response",
            groupId = "main-service",
            containerFactory = "aiResponseListenerFactory"
    )
    public void consumeAiResponse(KafkaAiResponse response) {
//        MessageEntity message = new MessageEntity();
//        message.setType("assistant_text");
//        System.out.println("Message data \n messageId: " + response.getMessageId() + " userId: " + response.getUserId() + " originalText: " + response.getOriginalText() + " summary: " + response.getSummary());
//        message.setUserId(response.getUserId());
//        message.setText(response.getSummary());
//        message.setCreatedAt(Instant.now());
//        messageRepository.save(message);
//        noteService.handleNoteCreation(response);
//        ws.sendToUser(response.getUserId(), message);
        try {
            System.out.println("====== RECEIVED AI RESPONSE ======");
            System.out.println("Message ID: " + response.getMessageId());
            System.out.println("User ID: " + response.getUserId());
            System.out.println("==================================");

            messageService.handleAiResponse(response);

        } catch (Exception e) {
            System.err.println("Error consuming AI response: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

