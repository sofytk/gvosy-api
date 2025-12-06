package com.sonchasapps.dto;

import java.util.Map;
import java.util.UUID;

public record KafkaAiResponse(
        String messageId,
        UUID userId,
        UUID assistantId,
        String originalText,
        String summary,
        String type,
        Map<String, Object> metadata
) {
    public KafkaAiResponse {}

    @Override
    public String messageId() {
        return messageId;
    }

    @Override
    public UUID userId() {
        return userId;
    }

    @Override
    public UUID assistantId() {
        return assistantId;
    }

    @Override
    public String originalText() {
        return originalText;
    }

    @Override
    public String summary() {
        return summary;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public Map<String, Object> metadata() {
        return metadata;
    }
}
