package com.sonchasapps.dto;

import com.sonchasapps.models.messages.MessageEntity;
import com.sonchasapps.models.messages.MessageRole;
import com.sonchasapps.models.messages.MessageStatus;

import java.time.Instant;
import java.util.UUID;

public record MessageDTO(
        String id,
        UUID userId,
        String conversationId,
        MessageRole role,
        String content,
        String audioUrl,
        MessageStatus status,
        UUID assistantId,
        Long noteId,
        Instant createdAt
) {
    public static MessageDTO fromEntity(MessageEntity e) {
        return new MessageDTO(
                e.getId(),
                e.getUserId(),
                e.getConversationId(),
                e.getRole(),
                e.getContent(),
                e.getAudioUrl(),
                e.getStatus(),
                e.getAssistantId(),
                e.getNoteId(),
                e.getCreatedAt()
        );
    }
}
