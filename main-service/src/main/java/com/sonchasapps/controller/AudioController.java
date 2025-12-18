package com.sonchasapps.controller;

import com.sonchasapps.dto.AudioUploadResponse;
import com.sonchasapps.dto.MessageDTO;
import com.sonchasapps.service.AudioStorageService;
import com.sonchasapps.service.MessageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/assistant/")
public class AudioController {

    private final AudioStorageService audioStorageService;
    private final MessageService messageService;

    public AudioController(AudioStorageService audioStorageService, MessageService messageService) {
        this.audioStorageService = audioStorageService;
        this.messageService = messageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.ALL_VALUE)
    public AudioUploadResponse uploadAudio(
            @RequestHeader(value = "X-User-Id", required = false) String userIdStr,
            @RequestParam("audio") String file,
            @RequestParam("assistantId") UUID assistantId
    ) {
        System.out.println("=== AssistantController /getAssistant ===");
        System.out.println("X-User-Id header: " + userIdStr);

        if (userIdStr == null || userIdStr.isEmpty()) {
            System.out.println("ERROR: X-User-Id header is missing");
        }

        UUID userId = null;
        try {
            userId = UUID.fromString(userIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Invalid UUID format: " + userIdStr);
        }
        String audioId = file;
        //String audioId = audioStorageService.store(file);
        MessageDTO msg = messageService.createAudioMessage(userId, assistantId, audioId);
        return new AudioUploadResponse(audioId, msg.id());
    }
}
