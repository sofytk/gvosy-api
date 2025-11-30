package com.sonchasapps.controller;

import com.sonchasapps.dto.AssistantRequest;
import com.sonchasapps.dto.AssistantResponse;
import com.sonchasapps.service.AssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
public class AssistantController {

    private final AssistantService service;

    @PostMapping("/add")
    public ResponseEntity<AssistantResponse> addAssistant(
            @RequestHeader(value = "X-User-Id", required = false) String userIdStr,
            @RequestBody AssistantRequest request
    ) {
        System.out.println("=== AssistantController /add ===");
        System.out.println("X-User-Id header: " + userIdStr);
        System.out.println("Request body: " + request);

        if (userIdStr == null || userIdStr.isEmpty()) {
            System.out.println("ERROR: X-User-Id header is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UUID userId;
        try {
            userId = UUID.fromString(userIdStr);
            System.out.println("Parsed userId: " + userId);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Invalid UUID format: " + userIdStr);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        AssistantResponse response = service.createAssistant(userId, request);
        System.out.println("SUCCESS: Assistant created");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAssistant")
    public ResponseEntity<AssistantResponse> getAssistant(
            @RequestHeader(value = "X-User-Id", required = false) String userIdStr
    ) {
        System.out.println("=== AssistantController /getAssistant ===");
        System.out.println("X-User-Id header: " + userIdStr);

        if (userIdStr == null || userIdStr.isEmpty()) {
            System.out.println("ERROR: X-User-Id header is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UUID userId;
        try {
            userId = UUID.fromString(userIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Invalid UUID format: " + userIdStr);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        AssistantResponse response = service.getAssistantById(userId);
        return ResponseEntity.ok(response);
    }
}