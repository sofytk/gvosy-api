package com.sonchasapps.dto;

import java.util.Map;
import java.util.UUID;


public record KafkaAiResponse(
        String messageId,
        UUID userId,
        String originalText,
        String summary
) {
}
