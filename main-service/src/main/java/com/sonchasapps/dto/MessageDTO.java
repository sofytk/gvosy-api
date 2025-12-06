package com.sonchasapps.dto;

import com.sonchasapps.models.MessageEntity;
import java.time.Instant;
import java.util.UUID;

public record MessageDTO(
        String id,
        UUID assistantId,
        UUID userId,
        String type,
        String content,
        String audioId,
        Instant createdAt
) {
    public static MessageDTO fromEntity(MessageEntity e) {
        return new MessageDTO(
                e.getId(),
                e.getAssistantId(),
                e.getUserId(),
                e.getType(),
                e.getText(),
                e.getAudioUrl(),
                e.getCreatedAt()
        );
    }
}

