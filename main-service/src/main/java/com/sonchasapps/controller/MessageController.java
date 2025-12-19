package com.sonchasapps.controller;

import com.sonchasapps.dto.MessageDTO;
import com.sonchasapps.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/voice_message")
    public ResponseEntity<MessageDTO> sendVoiceMessage(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam String audioUrl,
            @RequestParam(required = false) String conversationId
    ) {
        MessageDTO message = messageService.createAudioMessage(
                userId, audioUrl, conversationId
        );
        return ResponseEntity.ok(message);
    }


    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageDTO>> getConversation(
            @PathVariable String conversationId
    ) {
        return ResponseEntity.ok(messageService.getConversation(conversationId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<MessageDTO>> getUserMessages(
            @RequestHeader("X-User-Id") UUID userId
    ) {
        return ResponseEntity.ok(messageService.getUserMessages(userId));
    }
}
