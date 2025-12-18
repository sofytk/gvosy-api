package com.sonchasapps.service;

import com.sonchasapps.dto.KafkaAiRequest;
import com.sonchasapps.dto.PendingTranscription;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PendingTranscriptionStore {

    private final Map<String, PendingTranscription> pendingRequests = new ConcurrentHashMap<>();

    public void store(String operationId, KafkaAiRequest request) {
        pendingRequests.put(operationId, new PendingTranscription(
                operationId,
                request,
                System.currentTimeMillis()
        ));
        System.out.println("Stored pending transcription: " + operationId);
    }

    public Optional<PendingTranscription> get(String operationId) {
        return Optional.ofNullable(pendingRequests.get(operationId));
    }

    public void remove(String operationId) {
        pendingRequests.remove(operationId);
        System.out.println("Removed pending transcription: " + operationId);
    }

    public int size() {
        return pendingRequests.size();
    }

    @Scheduled(fixedRate = 300000)
    public void cleanupOldRequests() {
        long cutoff = System.currentTimeMillis() - 3600000; // 1 час
        int removed = 0;

        pendingRequests.entrySet().removeIf(entry -> {
            if (entry.getValue().timestamp() < cutoff) {
                System.out.println("Cleaning up old pending transcription: " + entry.getKey());
                return true;
            }
            return false;
        });

        if (removed > 0) {
            System.out.println("Cleaned up " + removed + " old pending requests");
        }
    }
}