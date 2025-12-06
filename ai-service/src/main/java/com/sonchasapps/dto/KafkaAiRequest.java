package com.sonchasapps.dto;

import java.util.UUID;

public record KafkaAiRequest(
        String messageId,
        UUID userId,
        UUID assistantId,
        String audioURL
) {
    public KafkaAiRequest {}

    @Override
    public UUID userId() {
        return userId;
    }

    @Override
    public String messageId() {
        return messageId;
    }

    @Override
    public UUID assistantId() {
        return assistantId;
    }

    @Override
    public String audioURL() {
        return audioURL;
    }
}

