package com.sonchasapps.dto;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class KafkaAiResponse {
    private String messageId;
    private UUID userId;
    private UUID assistanceId;
    private String originalText;
    private String summary;
    private String type;
    private Map<String, Object> metadata;

    public KafkaAiResponse(UUID userId, String messageId, UUID assistanceId, String originalText, String summary, String type, Map<String, Object> metadata) {
        this.userId = userId;
        this.assistanceId = assistanceId;
        this.messageId = messageId;
        this.originalText = originalText;
        this.summary = summary;
        this.type = type;
        this.metadata = metadata;
    }
    public KafkaAiResponse(){}

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(UUID assistanceId) {
        this.assistanceId = assistanceId;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}

