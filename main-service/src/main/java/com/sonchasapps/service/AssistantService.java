package com.sonchasapps.service;

import com.sonchasapps.dto.AssistantRequest;
import com.sonchasapps.dto.AssistantResponse;
import com.sonchasapps.models.AssistantEntity;
import com.sonchasapps.repository.AssistantRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AssistantService {
    private final AssistantRepository repo;

    public AssistantService(AssistantRepository repo) {
        this.repo = repo;
    }

    public AssistantResponse createAssistant(UUID userId, AssistantRequest request) {
        repo.findByUserId(userId).ifPresent(a -> {
            throw new RuntimeException("Assistant already exists for this user");
        });
        AssistantEntity assistant = new AssistantEntity(request.getAssistantName(), request.getAssistantAge(), request.getAssistantSex(), request.getAssistantDescription(), userId);
        repo.save(assistant);
        AssistantResponse response = new AssistantResponse(assistant.getAssistantName(), assistant.getAssistantAge(), assistant.getAssistantDesc(), assistant.getAssistantSex(), assistant.getAssistantImg());
        return response;
    }

    public AssistantResponse getAssistantByUserId(UUID userId) {
        AssistantEntity assistant = repo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Assistant not found"));
        AssistantResponse response = new AssistantResponse(assistant.getAssistantName(), assistant.getAssistantAge(), assistant.getAssistantDesc(), assistant.getAssistantSex(), assistant.getAssistantImg());
        return response;
    }

    public UUID getAssistantIdByUserId(UUID userId) {
        AssistantEntity assistant = repo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Assistant not found"));
        return assistant.getId();
    }


    public AssistantResponse getAssistantById(UUID id) {
        AssistantEntity assistant = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Assistant not found"));
        AssistantResponse response = new AssistantResponse(assistant.getAssistantName(), assistant.getAssistantAge(), assistant.getAssistantDesc(), assistant.getAssistantSex(), assistant.getAssistantImg());
        return response;
    }
}
